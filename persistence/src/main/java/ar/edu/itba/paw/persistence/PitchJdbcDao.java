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
			Optional<String> location, int page) {
		int offset = (page - 1) * MAX_ROWS;
		int presentFields = 0;
		List<Object> list = new ArrayList<>();
		Filter[] params = { 
				new Filter("pitchname", name.orElse("")),
				new Filter("sport", sport.orElse("")),
				new Filter("location", location.orElse(""))
		};
		StringBuilder queryString = new StringBuilder("SELECT * FROM pitches NATURAL JOIN clubs ");
		for(Filter param : params) {
			if(!((String)param.getValue()).isEmpty()) {
				queryString.append(buildPrefix(presentFields));
				queryString.append(param.queryAsString());
				list.add(param.getValue());
				presentFields++;
			}
		}
		queryString.append(" OFFSET ? ;");
		list.add(offset);
		return jdbcTemplate.query(queryString.toString(), prm, list.toArray());
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
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
	public void deletePitch(final long pitchid) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid IN "
				+ " (SELECT eventid FROM events WHERE pitchid = ?)", pitchid);
		jdbcTemplate.update("DELETE FROM events WHERE pitchid = ?", pitchid);
		jdbcTemplate.update("DELETE FROM pitches WHERE pitchid = ?", pitchid);
	}

}
