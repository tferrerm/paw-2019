package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.rowmapper.EventRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.UserRowMapper;

@Repository
public class EventJdbcDao implements EventDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private final SimpleJdbcInsert jdbcInscriptionInsert; // PREGUNTARRRRRRRRRRRRRRRRRRRRRRRRRR
	private static final int MAX_ROWS = 10;
	
	/*private static final RowMapper<Event> ROW_MAPPER = (rs, rowNum) ->
		new Event(rs.getLong("eventid"), rs.getString("name"), rs.getString("location"),
				rs.getString("description"), rs.getTimestamp("starts_at"), rs.getTimestamp("ends_at"),
				rs.getTimestamp("created_at"), rs.getTimestamp("deleted_at"));*/
	
	@Autowired
	private EventRowMapper erm;
	
	@Autowired
	private UserRowMapper urm;

	@Autowired
	public EventJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("events")
				.usingGeneratedKeyColumns("eventid");
		jdbcInscriptionInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("events_users");
	}
	
	@Override
	public Optional<Event> findByEventId(long eventid) {
		return jdbcTemplate.query("SELECT * FROM events JOIN users ON events.userid = users.userid"
				+ " WHERE eventid = ?", erm, eventid)
				.stream().findAny();
	}

	@Override
	public List<Event> findByUsername(String username, int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM events NATURAL JOIN events_users JOIN users ON events.userid = users.userid"
				+ " WHERE username = ?"
				+ " OFFSET ?", erm, username, offset);
	}
	
	@Override
	public int countUserEventPages(final long userid) {
		Integer rows = jdbcTemplate.queryForObject("SELECT count(*) FROM events_users WHERE userid = ?",
				Integer.class, userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}
	
	@Override
	public List<Event> findFutureEvents(int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM events JOIN users ON events.userid = users.userid"
				+ " WHERE starts_at > ?"
				+ " OFFSET ?", erm, Timestamp.from(Instant.now()), offset);
	}
	
	@Override
	public int countFutureEventPages() {
		Integer rows = jdbcTemplate.queryForObject("SELECT count(*) FROM events WHERE "
				+ " starts_at > ?",	Integer.class, Timestamp.from(Instant.now()));
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public Event create(String name, User owner, String location, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt) {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("name", name);
		args.put("userid", owner.getUserid());
		args.put("location", location);
		args.put("description", description);
		args.put("max_participants", maxParticipants);
		args.put("starts_at", Timestamp.from(startsAt));
		args.put("ends_at", Timestamp.from(endsAt));
		args.put("created_at", Timestamp.from(now));
		args.put("deleted_at", null);
		final Number eventId = jdbcInsert.executeAndReturnKey(args);
		return new Event(eventId.longValue(), name, owner, location, description, 
				maxParticipants, startsAt, endsAt);
	}
	
	@Override
	public int countParticipants(long eventid) {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM events_users WHERE "
				+ " eventid = ?", Integer.class, eventid);
	}

	@Override // BOOLEANNNNNNNNNNNNNNNNNNNN
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException {
		final Map<String, Object> args = new HashMap<>();
		args.put("userid", user.getUserid());
		args.put("eventid", event.getEventId());
		try {
			jdbcInscriptionInsert.execute(args);
		} catch(DuplicateKeyException e) {
			throw new UserAlreadyJoinedException("User " + user.getUserid() + " already joined event "
					+ event.getEventId());
		}
		return true;
	}

	@Override
	public List<User> findEventUsers(final long eventid, final int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM events_users NATURAL JOIN users "
				+ " WHERE eventid = ? OFFSET ?", urm, eventid, offset);
	}

}
