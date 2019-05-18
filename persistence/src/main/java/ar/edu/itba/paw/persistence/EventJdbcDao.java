package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.rowmapper.ClubRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.EventListRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.EventRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.InscriptionsRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.UserRowMapper;

@Repository
public class EventJdbcDao implements EventDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private final SimpleJdbcInsert jdbcInscriptionInsert; // PREGUNTARRRRRRRRRRRRRRRRRRRRRRRRRR
	private static final int MAX_ROWS = 10;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	/*private static final RowMapper<Event> ROW_MAPPER = (rs, rowNum) ->
		new Event(rs.getLong("eventid"), rs.getString("name"), rs.getString("location"),
				rs.getString("description"), rs.getTimestamp("starts_at"), rs.getTimestamp("ends_at"),
				rs.getTimestamp("created_at"), rs.getTimestamp("deleted_at"));*/
	
	@Autowired
	private EventRowMapper erm;
	
	@Autowired
	private UserRowMapper urm;
	
	@Autowired
	private EventListRowMapper elrm;
	
	@Autowired
	private InscriptionsRowMapper irm;
	
	@Autowired
	private ClubRowMapper crm;

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
		return jdbcTemplate.query("SELECT * FROM events NATURAL JOIN pitches NATURAL JOIN clubs "
				+ " JOIN users ON events.userid = users.userid "
				+ " WHERE eventid = ?", erm, eventid)
				.stream().findAny();
	}

	@Override
	public List<Event> findByUsername(boolean futureEvents, String username, int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		Instant now = Instant.now();
		StringBuilder query = new StringBuilder("SELECT * FROM events NATURAL JOIN pitches "
				+ " NATURAL JOIN clubs NATURAL JOIN users WHERE username = ? AND starts_at ");
		query.append((futureEvents) ? " > ? " : " < ? ");
		query.append(" OFFSET ?");
		return jdbcTemplate.query(query.toString(), erm, username, Timestamp.from(now), offset);
	}
	
	@Override
	public List<Event> findByOwner(boolean futureEvents, String username, int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		Instant now = Instant.now();
		StringBuilder query = new StringBuilder("SELECT * FROM events NATURAL JOIN pitches NATURAL JOIN users NATURAL JOIN clubs "
				+ " WHERE username = ? AND starts_at ");
		query.append((futureEvents) ? " > ? " : " < ? ");
		query.append(" OFFSET ?");
		return jdbcTemplate.query(query.toString(), erm, username, Timestamp.from(now), offset);
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
		return jdbcTemplate.query("SELECT * FROM events NATURAL JOIN pitches NATURAL JOIN clubs "
				+ " JOIN users ON events.userid = users.userid WHERE starts_at > ?"
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
	public List<Event> findCurrentEventsInPitch(final long pitchid) {
		LocalDate ld = LocalDate.now();
		// Today at 00:00
		Instant today = ld.atStartOfDay().atZone(ZoneId.of(TIME_ZONE)).toInstant();
		// In seven days at 23:00
		Instant inAWeek = today.plus(8, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS);
		return jdbcTemplate.query("SELECT * FROM events NATURAL JOIN pitches NATURAL JOIN clubs "
				+ " NATURAL JOIN users WHERE pitchid = ? "
				+ " AND starts_at > ? "
				+ " AND starts_at < ? ", erm, pitchid,
					Timestamp.from(today), Timestamp.from(inAWeek));
	}
	
	@Override
	public List<Event> findBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<String> sport, Optional<Integer> vacancies, int page) {
		int offset = (page - 1) * MAX_ROWS;
		int presentFields = 0;
		List<Object> list = new ArrayList<>();
		Filter[] params = { 
				new Filter("eventname", name.orElse(null)),
				new Filter("clubname", establishment.orElse(null)),
				new Filter("sport", sport.orElse(null)),
				new Filter("customVacanciesFilter", vacancies.orElse(null)),
				new Filter("starts_at", (onlyFuture)? Timestamp.from(Instant.now()) : null)
		};
		StringBuilder queryString = new StringBuilder("SELECT * FROM (events NATURAL JOIN pitches NATURAL JOIN clubs) AS t ");
		for(Filter param : params) {
			if(!isEmpty(param.getValue())) {
				queryString.append(buildPrefix(presentFields));
				switch(param.getName()) {
				case "customVacanciesFilter":
					queryString.append(" ? <= max_participants - (SELECT count(*) FROM events_users WHERE eventid = t.eventid) ");
					break;
				case "starts_at":
					queryString.append(param.queryAsGreaterInteger(true));
					break;
				default:
					queryString.append(param.queryAsString());
					break;
				}
				list.add(param.getValue());
				presentFields++;
			}
		}
		queryString.append(" OFFSET ? ;");
		list.add(offset);
		return jdbcTemplate.query(queryString.toString(), elrm, list.toArray());
	}
	
	@Override
	public List<Long[]> countBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<String> sport, Optional<Integer> vacancies, int page) {
		int offset = (page - 1) * MAX_ROWS;
		int presentFields = 0;
		List<Object> list = new ArrayList<>();
		Filter[] params = { 
				new Filter("e.eventname", name.orElse(null)),
				new Filter("clubname", establishment.orElse(null)),
				new Filter("sport", sport.orElse(null)),
				new Filter("customVacanciesFilter", vacancies.orElse(null)),
				new Filter("e.starts_at", (onlyFuture)? Timestamp.from(Instant.now()) : null)
		};
		StringBuilder queryString = new StringBuilder("SELECT e.eventid, count(evu.eventid) FROM "
				+ " events AS e NATURAL JOIN pitches NATURAL JOIN clubs LEFT OUTER JOIN events_users AS evu "
				+ " ON e.eventid = evu.eventid ");
		for(Filter param : params) {
			if(!isEmpty(param.getValue())) {
				queryString.append(buildPrefix(presentFields));
				switch(param.getName()) {
				case "customVacanciesFilter":
					queryString.append(" ? <= max_participants - (SELECT count(*) "
							+ " FROM events_users AS eu WHERE eu.eventid = e.eventid) ");
					break;
				case "e.starts_at":
					queryString.append(param.queryAsGreaterInteger(true));
					break;
				default:
					queryString.append(param.queryAsString());
					break;
				}
				list.add(param.getValue());
				presentFields++;
			}
		}
		queryString.append(" GROUP BY e.eventid OFFSET ? ;");
		list.add(offset);
		return jdbcTemplate.query(queryString.toString(), irm, list.toArray());
	}
	
	private boolean isEmpty(Object value) {
		return value == null;
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}

	@Override
	public Event create(final String name, final User owner, final Pitch pitch, 
			final String description, final int maxParticipants, 
			final Instant startsAt, final Instant endsAt) 
				throws EventOverlapException {
		
		Timestamp eventStartsAt = Timestamp.from(startsAt);
		Timestamp eventEndsAt = Timestamp.from(endsAt);
		
		String eventOverlapQueryString = "SELECT count(*) FROM events WHERE pitchid = ? AND "
				+ " ((starts_at <= ? AND ends_at > ?) OR (starts_at > ? AND starts_at < ?))";
		
		int eventOverlapQueryResult = jdbcTemplate.queryForObject(eventOverlapQueryString, 
				Integer.class, pitch.getPitchid(), eventStartsAt, eventStartsAt, eventStartsAt, 
				eventEndsAt);
		
		if(eventOverlapQueryResult > 0)
			throw new EventOverlapException("Pitch is already taken in the chosen time period");
		
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("eventname", name);
		args.put("userid", owner.getUserid());
		args.put("pitchid", pitch.getPitchid());
		args.put("description", description);
		args.put("max_participants", maxParticipants);
		args.put("starts_at", Timestamp.from(startsAt));
		args.put("ends_at", Timestamp.from(endsAt));
		args.put("event_created_at", Timestamp.from(now));
		final Number eventId = jdbcInsert.executeAndReturnKey(args);
		
		return new Event(eventId.longValue(), name, owner, pitch, description, 
				maxParticipants, startsAt, endsAt);
	}
	
	@Override
	public int countParticipants(final long eventid) {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM events_users WHERE "
				+ " eventid = ?", Integer.class, eventid);
	}

	@Override // BOOLEANNNNNNNNNNNNNNNNNNNN
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, UserBusyException {
		
		Timestamp eventStartsAt = Timestamp.from(event.getStartsAt());
		Timestamp eventEndsAt = Timestamp.from(event.getEndsAt());
		
		String userBusyQueryString = "SELECT count(*) FROM events_users AS eu "
				+ " INNER JOIN events AS e ON eu.eventid = e.eventid WHERE eu.userid = ? AND "
				+ " ((starts_at <= ? AND ends_at > ?) OR (starts_at > ? AND starts_at < ?))";
		
		int userBusyQueryResult = jdbcTemplate.queryForObject(userBusyQueryString, Integer.class,
				user.getUserid(), eventStartsAt, eventStartsAt, eventStartsAt, eventEndsAt);
		
		if(userBusyQueryResult > 0)
			throw new UserBusyException("User " + user.getUserid() + " already joined "
					+ "an event in that period");
		
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
	public void leaveEvent(final User user, final Event event) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid = ? AND userid = ?",
				event.getEventId(), user.getUserid());
	}
	
	@Override
	public int kickFromEvent(final long kickedUserId, final long eventId) {
		return jdbcTemplate.update("DELETE FROM events_users " +
				" WHERE eventid = ? AND userid = ?", eventId, kickedUserId);
	}

	@Override
	public List<User> findEventUsers(final long eventid, final int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		return jdbcTemplate.query("SELECT * FROM events_users NATURAL JOIN users "
				+ " WHERE eventid = ? OFFSET ?", urm, eventid, offset);
	}
	
	@Override
	public int countUserEvents(boolean isCurrentEventsQuery, final long userid) {
		Integer userEvents = 0;
		StringBuilder query = new StringBuilder("SELECT count(*) FROM events_users NATURAL JOIN events "
				+ " WHERE userid = ? AND ends_at ");
		query.append((isCurrentEventsQuery) ? " > ? " : " <= ? ");
		userEvents = jdbcTemplate.queryForObject(query.toString(), Integer.class,
					userid, Timestamp.from(Instant.now()));
		return userEvents;
	}
	
	@Override
	public int countUserOwnedCurrEvents(final long userid) {
		Integer userOwnerEvents = jdbcTemplate.queryForObject(
				"SELECT count(*) FROM events WHERE userid = ? AND ends_at > ?",
				Integer.class, userid, Timestamp.from(Instant.now()));
		return userOwnerEvents;
	}
	
	@Override
	public Sport getFavoriteSport(final long userid) {
		String queryString = "SELECT sport FROM events_users NATURAL JOIN events NATURAL JOIN pitches "
				+ " WHERE userid = ? GROUP BY sport HAVING count(*) >= ANY (SELECT count(*) "
				+ " FROM events_users NATURAL JOIN events NATURAL JOIN pitches WHERE userid = ? GROUP BY sport) "
				+ " ORDER BY sport ASC)";
		return jdbcTemplate.queryForObject(queryString, Sport.class, userid, userid);
	}
	
	@Override
	public Club getFavoriteClub(final long userid) {
		String queryString = " SELECT clubid FROM events_users NATURAL JOIN events NATURAL JOIN pitches "
				+ " WHERE userid = ? GROUP BY clubid HAVING count(*) >= ANY (SELECT count(*) "
				+ " FROM events_users NATURAL JOIN events NATURAL JOIN pitches WHERE userid = ? GROUP BY clubid)"
				+ " ORDER BY clubid ASC) ";
		return jdbcTemplate.queryForObject(queryString, crm, userid, userid);
	}
	
	@Override
	public void deleteEvent(final long eventid) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid = ?", eventid);
		jdbcTemplate.update("DELETE FROM events WHERE eventid = ?", eventid);
	}

}
