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

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.persistence.rowmapper.PitchRowMapper;

@Repository
public class PitchJdbcDao implements PitchDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 10;
	
	@Autowired
	private PitchRowMapper prm;
	
	@Autowired
	public PitchJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("pitches")
				.usingGeneratedKeyColumns("pitchid");
	}

	@Override
	public Optional<Pitch> findById(long pitchid) {
		return jdbcTemplate.query("SELECT * FROM pitches p NATURAL JOIN clubs c "
				+ " WHERE p.pitchid = ?", prm, pitchid)
				.stream().findAny();
	}

	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		int offset = (page - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM pitches p NATURAL JOIN clubs c "
				+ " WHERE p.clubid = ? OFFSET ?",
				prm, clubid, offset);
	}
	
	@Override
	public List<Pitch> findBy(Optional<String> name, Optional<String> sport,
			Optional<String> location, Optional<String> clubName, int page) {
		
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT * ");
		queryString.append(getFilterQueryEndString(paramValues, name, 
				sport, location, clubName));
		
		int offset = (page - 1) * MAX_ROWS;
		queryString.append(" OFFSET ? ;");
		paramValues.add(offset);
		
		return jdbcTemplate.query(queryString.toString(), prm, paramValues.toArray());
	}
	
	@Override
	public Integer countFilteredPitches(final Optional<String> pitchName, 
			final Optional<String> sport, final Optional<String> location, 
			final Optional<String> clubName) {
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT count(*) ");
		queryString.append(getFilterQueryEndString(paramValues, pitchName, 
				sport, location, clubName));
		
		return jdbcTemplate.queryForObject(queryString.toString(), Integer.class, paramValues.toArray());
	}
	
	private String getFilterQueryEndString(List<Object> paramValues, final Optional<String> pitchName, 
			final Optional<String> sport, final Optional<String> location, 
			final Optional<String> clubName) {
		int presentFields = 0;
		Filter[] params = { 
				new Filter("pitchname", pitchName.orElse("")),
				new Filter("sport", sport.orElse("")),
				new Filter("location", location.orElse("")),
				new Filter("clubname", clubName.orElse(""))
		};
		StringBuilder queryString = new StringBuilder(" FROM pitches NATURAL JOIN clubs ");
		for(Filter param : params) {
			if(!((String)param.getValue()).isEmpty()) {
				queryString.append(buildPrefix(presentFields));
				queryString.append(param.queryAsString());
				paramValues.add(param.getValue());
				presentFields++;
			}
		}
		return queryString.toString();
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}
	
	@Override
	public int countPitchPages() {
		Integer rows = jdbcTemplate.queryForObject("SELECT count(*) FROM pitches ",	Integer.class);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public Pitch create(Club club, String name, Sport sport) {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("clubid", club.getClubid());
		args.put("pitchname", name);
		args.put("sport", sport.toString());
		args.put("pitch_created_at", Timestamp.from(now));
		final Number pitchId = jdbcInsert.executeAndReturnKey(args);
		return new Pitch(pitchId.longValue(), club, name, sport, now);
	}
	
	@Override
	public int getPageInitialPitchIndex(final int pageNum) {
		return (pageNum -1) * MAX_ROWS + 1;
	}
	
	@Override
	public void deletePitch(final long pitchid) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid IN "
				+ " (SELECT eventid FROM events WHERE pitchid = ?)", pitchid);
		jdbcTemplate.update("DELETE FROM events WHERE pitchid = ?", pitchid);
		jdbcTemplate.update("DELETE FROM pitches WHERE pitchid = ?", pitchid);
	}

}
