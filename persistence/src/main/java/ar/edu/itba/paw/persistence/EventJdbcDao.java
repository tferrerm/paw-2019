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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Event;

@Repository
public class EventJdbcDao implements EventDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 10;
	
	private static final RowMapper<Event> ROW_MAPPER = (rs, rowNum) ->
		new Event(rs.getLong("eventid"), rs.getString("name"), rs.getString("location"),
				rs.getString("description"), rs.getTimestamp("starts_at"), rs.getTimestamp("ends_at"),
				rs.getTimestamp("created_at"), rs.getTimestamp("deleted_at"));

	@Autowired
	public EventJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("events")
				.usingGeneratedKeyColumns("eventid");
		
	}
	@Override
	public Optional<Event> findByEventId(long eventid) {
		return jdbcTemplate.query("SELECT * FROM events WHERE eventid = ?", ROW_MAPPER, eventid)
				.stream().findAny();
	}

	@Override
	public List<Event> findByUsername(String username) {
		return jdbcTemplate.query("SELECT * FROM events NATURAL JOIN events_users NATURAL JOIN users"
				+ " WHERE username = ?", ROW_MAPPER, username);
	}
	
	@Override
	public List<Event> findFutureEvents() {
		return jdbcTemplate.query("SELECT * FROM events WHERE starts_at > ?",
				ROW_MAPPER, Timestamp.from(Instant.now()));
	}

	@Override
	public Event create(String name, String location, String description, Instant startsAt, Instant endsAt) {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("name", name);
		args.put("location", location);
		args.put("description", description);
		args.put("starts_at", Timestamp.from(startsAt));
		args.put("ends_at", Timestamp.from(endsAt));
		args.put("created_at", Timestamp.from(now));
		args.put("deleted_at", null);
		final Number eventId = jdbcInsert.executeAndReturnKey(args);
		return new Event(eventId.longValue(), name, location, description, startsAt, endsAt);
	}

}
