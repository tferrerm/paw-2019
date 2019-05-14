package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
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
	public Club create(long userId, String name, String location) {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("userid",userId);
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

}
