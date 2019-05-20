package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.persistence.rowmapper.ClubRowMapper;

@Repository
public class ClubJdbcDao implements ClubDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 10;
	
	@Autowired
	private ClubRowMapper crm;
	
	@Autowired
	public ClubJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("clubs")
				.usingGeneratedKeyColumns("clubid");
	}

	@Override
	public Optional<Club> findById(long clubid) {
		return jdbcTemplate.query("SELECT * FROM clubs WHERE clubid = ?", crm, clubid)
				.stream().findAny();
	}
	
	@Override
	public List<Club> findAll(int page) {
		int offset = (page - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM clubs OFFSET ?", crm, offset);
	}

	@Override
	public Club create(String name, String location) {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("clubname", name);
		args.put("location", location);
		args.put("club_created_at", Timestamp.from(now));
		final Number clubId = jdbcInsert.executeAndReturnKey(args);
		return new Club(clubId.longValue(), name, location, now);
	}
	
	@Override
	public void deleteClub(final long clubid) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid IN "
				+ " (SELECT eventid FROM events WHERE pitchid IN "
				+ " (SELECT pitchid FROM pitches WHERE clubid = ? ))", clubid);
		jdbcTemplate.update("DELETE FROM events WHERE pitchid IN "
				+ " (SELECT pitchid FROM pitches WHERE clubid = ?)", clubid);
		jdbcTemplate.update("DELETE FROM pitches WHERE clubid = ?", clubid);
		jdbcTemplate.update("DELETE FROM clubs WHERE clubid = ?", clubid);
	}
	
	@Override
	public int getPageInitialClubIndex(final int pageNum) {
		return (pageNum -1) * MAX_ROWS + 1;
	}

	@Override
	public int countClubPages() {
		Integer rows = jdbcTemplate.queryForObject("SELECT count(*) FROM clubs", Integer.class);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page) {
		
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT * ");
		queryString.append(getFilterQueryEndString(paramValues, clubName, location));
		queryString.append(" ORDER BY clubname ASC ");
		
		int offset = (page - 1) * MAX_ROWS;
		queryString.append(" OFFSET ? ;");
		paramValues.add(offset);
		
		return jdbcTemplate.query(queryString.toString(), crm, paramValues.toArray());
	}
	
	@Override
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location) {
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT count(*) ");
		queryString.append(getFilterQueryEndString(paramValues, clubName, location));
		
		return jdbcTemplate.queryForObject(queryString.toString(), Integer.class, paramValues.toArray());
	}
	
	private String getFilterQueryEndString(List<Object> paramValues, final Optional<String> clubName, 
			final Optional<String> location) {
		Filter[] params = { 
				new Filter("LOWER(clubname)", clubName),
				new Filter("LOWER(location)", location)
		};
		StringBuilder queryString = new StringBuilder(" FROM clubs ");
		for(Filter param : params) {
			if(param.getValue().isPresent() && !isEmpty(param.getValue())) {
				queryString.append(buildPrefix(paramValues.size()));
				queryString.append(param.queryAsString());
				paramValues.add(param.getValue().get());
			}
		}
		return queryString.toString();
	}
	
	private boolean isEmpty(Optional<?> opt) {
		return opt.get().toString().isEmpty();
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}

	@Override
	public int countPastEvents(final long clubid) {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM events NATURAL JOIN pitches "
				+ " WHERE clubid = ? AND ends_at < ?", Integer.class, clubid, 
				Timestamp.from(Instant.now()));
	}

}
