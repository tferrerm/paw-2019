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
import ar.edu.itba.paw.persistence.rowmapper.EventRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.InscriptionsRowMapper;
import ar.edu.itba.paw.persistence.rowmapper.UserRowMapper;

public class EventJdbcDao implements EventDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private final SimpleJdbcInsert jdbcInscriptionInsert;
	private static final int MAX_ROWS = 10;
	private static final int MAX_EVENTS_PER_WEEK = 24 * 7;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Autowired
	private EventRowMapper erm;
	
	@Autowired
	private UserRowMapper urm;
	
	@Autowired
	private InscriptionsRowMapper irm;
	
	@Autowired
	private ClubRowMapper crm;

	@Autowired
	public EventJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
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
	public List<Event> findByOwner(final boolean futureEvents, final long userid, final int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		Instant now = Instant.now();
		StringBuilder queryString = new StringBuilder("SELECT * FROM events NATURAL JOIN pitches "
				+ " NATURAL JOIN users NATURAL JOIN clubs "
				+ " WHERE userid = ? AND starts_at ");
		queryString.append((futureEvents) ? " > ? ORDER BY starts_at ASC " : " <= ? ORDER BY starts_at DESC ");
		queryString.append(" LIMIT ? OFFSET ?");
		return jdbcTemplate.query(queryString.toString(), erm, userid, Timestamp.from(now),
				MAX_ROWS, offset);
	}
	
	@Override
	public int countByOwner(final boolean futureEvents, final long userid) {
		StringBuilder queryString = new StringBuilder("SELECT count(*) FROM events WHERE userid = ? "
				+ " AND starts_at ");
		queryString.append((futureEvents) ? " > ? " : " <= ? ");
		return jdbcTemplate.queryForObject(queryString.toString(), Integer.class, userid,
				Timestamp.from(Instant.now()));
	}
	
	public List<Event> findFutureUserInscriptions(long userid) {
		Instant now = Instant.now();
		StringBuilder query = new StringBuilder("SELECT * FROM (events NATURAL JOIN pitches "
				+ " NATURAL JOIN users NATURAL JOIN clubs) AS t "
				+ " WHERE EXISTS (SELECT eventid FROM events_users "
				+ " WHERE eventid = t.eventid AND userid = ?) AND t.starts_at "
				+ " > ? ORDER BY t.starts_at ASC LIMIT ? ");
		return jdbcTemplate.query(query.toString(), erm, userid, Timestamp.from(now), MAX_EVENTS_PER_WEEK);
	}
	
	public List<Event> findPastUserInscriptions(long userid, int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		Instant now = Instant.now();
		StringBuilder query = new StringBuilder("SELECT * FROM (events NATURAL JOIN pitches "
				+ " NATURAL JOIN users NATURAL JOIN clubs) AS t "
				+ " WHERE EXISTS (SELECT eventid FROM events_users "
				+ " WHERE eventid = t.eventid AND userid = ?) AND t.starts_at "
				+ " <= ? ORDER BY t.starts_at DESC LIMIT ? OFFSET ?");
		return jdbcTemplate.query(query.toString(), erm, userid, Timestamp.from(now), MAX_ROWS, offset);
	}
	
	@Override
	public Integer countByUserInscriptions(final boolean futureEvents, final long userid) {
		StringBuilder query = new StringBuilder("SELECT count(*) FROM events AS e "
				+ " WHERE EXISTS (SELECT * FROM events_users WHERE eventid = e.eventid "
				+ " AND userid = ?) AND e.starts_at ");
		query.append((futureEvents) ? " > ? " : " <= ? ");
		return jdbcTemplate.queryForObject(query.toString(), Integer.class, userid, 
				Timestamp.from(Instant.now()));
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
				+ " AND starts_at < ? LIMIT ?", erm, pitchid,
					Timestamp.from(today), Timestamp.from(inAWeek), MAX_EVENTS_PER_WEEK);
	}
	
	public List<Event> findBy(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies,
			Optional<Instant> date, final int pageNum) {
		
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT * ");
		queryString.append(getFilterQueryEndString(paramValues, onlyFuture, eventName, 
				clubName, sport, organizer, vacancies, date));
		queryString.append(" ORDER BY t.starts_at ASC, t.eventid ASC ");
		
		int offset = (pageNum - 1) * MAX_ROWS;
		queryString.append(" LIMIT ? OFFSET ? ;");
		paramValues.add(MAX_ROWS);
		paramValues.add(offset);
		
		return jdbcTemplate.query(queryString.toString(), erm, paramValues.toArray());
	}
	
	@Override
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies,
			final Optional<Instant> date) {
		
		List<Object> paramValues = new ArrayList<>();
		StringBuilder queryString = new StringBuilder("SELECT count(*) ");
		queryString.append(getFilterQueryEndString(paramValues, onlyFuture, eventName, 
				clubName, sport, organizer, vacancies, date));
		
		return jdbcTemplate.queryForObject(queryString.toString(), Integer.class, paramValues.toArray());
	}
	
	private String getFilterQueryEndString(List<Object> paramValues, final boolean onlyFuture, 
			final Optional<String> eventName, final Optional<String> clubName, 
			final Optional<String> sport, final Optional<String> organizer,
			final Optional<Integer> vacancies, final Optional<Instant> date) {
		Filter[] params = { 
				new Filter("LOWER(eventname)", eventName),
				new Filter("LOWER(clubname)", clubName),
				new Filter("LOWER(sport)", sport),
				new Filter("LOWER(firstname) || ' ' || LOWER(lastname)", organizer),
				new Filter("customVacanciesFilter", vacancies),
				new Filter("starts_at", Optional.ofNullable((date.isPresent()) ?
						Timestamp.from(date.get()) : null)),
				new Filter("futureFilter", 
						Optional.ofNullable((onlyFuture)? Timestamp.from(Instant.now()) : null))
		};
		StringBuilder queryString = new StringBuilder(" FROM (events NATURAL JOIN pitches "
				+ " NATURAL JOIN clubs NATURAL JOIN users) AS t ");
		
		for(Filter param : params) {
			if(param.getValue().isPresent()) {
				switch(param.getName()) {
				case "customVacanciesFilter":
					queryString.append(buildPrefix(paramValues.size()));
					queryString.append(" ? <= max_participants - (SELECT count(*) FROM events_users WHERE eventid = t.eventid) ");
					break;
				case "futureFilter":
					queryString.append(buildPrefix(paramValues.size()));
					queryString.append(" starts_at >= ? ");
					break;
				case "starts_at":
					queryString.append(buildPrefix(paramValues.size()));
					String criteria = "";//(date.isPresent()) 
						//? param.queryAsDateRange() : param.queryAsGreaterInteger(true);
					queryString.append(criteria);
					break;
				default:
					if(isEmpty(param.getValue()))
						continue;
					queryString.append(buildPrefix(paramValues.size()));
					//queryString.append(param.queryAsString());
					break;
				}
				paramValues.add(param.getValue().get());
			}
		}
		return queryString.toString();
	}
	
	public List<Long[]> countBy(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> establishment, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies, 
			Optional<Instant> date, final int pageNum) {
		int offset = (pageNum - 1) * MAX_ROWS;
		List<Object> list = new ArrayList<>();
		Filter[] params = { 
				new Filter("LOWER(e.eventname)", eventName),
				new Filter("LOWER(clubname)", establishment),
				new Filter("LOWER(sport)", sport),
				new Filter("LOWER(u.firstname) || ' ' || LOWER(u.lastname)", organizer),
				new Filter("customVacanciesFilter", vacancies),
				new Filter("e.starts_at", Optional.ofNullable((date.isPresent()) ?
						Timestamp.from(date.get()) : null)),
				new Filter("futureFilter", 
						Optional.ofNullable((onlyFuture)? Timestamp.from(Instant.now()) : null))
		};
		StringBuilder queryString = new StringBuilder("SELECT e.eventid, count(evu.eventid) FROM "
				+ " events AS e INNER JOIN users AS u ON e.userid = u.userid NATURAL JOIN pitches "
				+ " NATURAL JOIN clubs LEFT OUTER JOIN events_users AS evu ON e.eventid = evu.eventid ");
		
		for(Filter param : params) {
			if(param.getValue().isPresent()) {
				switch(param.getName()) {
				case "customVacanciesFilter":
					queryString.append(buildPrefix(list.size()));
					queryString.append(" ? <= max_participants - (SELECT count(*) "
							+ " FROM events_users AS eu WHERE eu.eventid = e.eventid) ");
					break;
				case "futureFilter":
					queryString.append(buildPrefix(list.size()));
					queryString.append(" e.starts_at >= ? ");
					break;
				case "e.starts_at":
					queryString.append(buildPrefix(list.size()));
					//queryString.append(param.queryAsDateRange());
					break;
				default:
					if(isEmpty(param.getValue()))
						continue;
					queryString.append(buildPrefix(list.size()));
					//queryString.append(param.queryAsString());
					break;
				}
				list.add(param.getValue().get());
			}
		}
		queryString.append(" GROUP BY e.eventid, e.starts_at ORDER BY e.starts_at ASC, e.eventid ASC OFFSET ? ;");
		list.add(offset);
		return jdbcTemplate.query(queryString.toString(), irm, list.toArray());
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}
	
	/**
	 * Check if optional is present first
	 * @param str Optional (not empty)
	 * @return
	 */
	private boolean isEmpty(Optional<?> opt) {
		return opt.get().toString().isEmpty();
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
		//final Number eventId = jdbcInsert.executeAndReturnKey(args);
		
		return new Event(name, owner, pitch, description, 
				maxParticipants, startsAt, endsAt, now);
	}
	
	@Override
	public int countParticipants(final long eventid) {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM events_users WHERE "
				+ " eventid = ?", Integer.class, eventid);
	}

	@Override
	public void joinEvent(final User user, final Event event)
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
	public int countUserEvents(boolean isCurrentEventsQuery, final long userid) {
		Integer userEvents = 0;
		StringBuilder query = new StringBuilder("SELECT count(*) FROM events_users AS eu INNER JOIN "
				+ " events AS e ON eu.eventid = e.eventid WHERE eu.userid = ? AND e.ends_at ");
		query.append((isCurrentEventsQuery) ? " > ? " : " <= ? ");
		userEvents = jdbcTemplate.queryForObject(query.toString(), Integer.class,
					userid, Timestamp.from(Instant.now()));
		return userEvents;
	}
	
	@Override
	public int countUserOwnedCurrEvents(final long userid) {
		Integer userOwnerEvents = jdbcTemplate.queryForObject(
				"SELECT count(*) FROM events WHERE userid = ? AND starts_at > ?",
				Integer.class, userid, Timestamp.from(Instant.now()));
		return userOwnerEvents;
	}
	
	@Override
	public Optional<Sport> getFavoriteSport(final long userid) {
		String queryString = "SELECT sport FROM events_users NATURAL JOIN events NATURAL JOIN pitches "
				+ " WHERE userid = ? GROUP BY sport HAVING count(*) >= ANY (SELECT count(*) "
				+ " FROM events_users NATURAL JOIN events NATURAL JOIN pitches WHERE userid = ? GROUP BY sport) "
				+ " ORDER BY sport ASC";
		Optional<String> sp = jdbcTemplate.query(queryString,
				(rs, rowNum) -> rs.getString("sport"), userid, userid)
					.stream().findFirst();
		if(!sp.isPresent())
			return Optional.empty();
		return Optional.of(Sport.valueOf(sp.get()));
	}
	
	@Override
	public Optional<Club> getFavoriteClub(final long userid) {
		String queryString = " SELECT clubid, clubname, location, club_created_at FROM events_users "
				+ " NATURAL JOIN events NATURAL JOIN pitches NATURAL JOIN clubs "
				+ " WHERE userid = ? GROUP BY clubid, clubname, location, club_created_at HAVING count(*) >= ANY (SELECT count(*) "
				+ " FROM events_users NATURAL JOIN events NATURAL JOIN pitches WHERE userid = ? GROUP BY clubid)"
				+ " ORDER BY clubid ASC ";
		return jdbcTemplate.query(queryString, crm, userid, userid).stream().findFirst();
	}
	
	@Override
	public int getPageInitialEventIndex(final int pageNum) {
		return (pageNum - 1) * MAX_ROWS + 1;
	}
	
	@Override
	public void deleteEvent(final long eventid) {
		jdbcTemplate.update("DELETE FROM events_users WHERE eventid = ?", eventid);
		jdbcTemplate.update("DELETE FROM events WHERE eventid = ?", eventid);
	}

	@Override
	public Optional<Integer> getVoteBalance(final long eventid) {
		return jdbcTemplate.query("SELECT sum(vote) AS s FROM events_users "
				+ " WHERE eventid = ? ", (rs, rowNum) -> rs.getInt("s"), eventid)
					.stream().findFirst();
	}

	@Override
	public Optional<Integer> getUserVote(final long eventid, final long userid) {
		return jdbcTemplate.query("SELECT vote FROM events_users "
				+ " WHERE eventid = ? AND userid = ? ", 
				(rs, rowNum) -> rs.getInt("vote"), eventid, userid)
					.stream().findFirst();
	}

	@Override
	public int vote(final boolean isUpvote, final long eventid, final long userid) {
		return jdbcTemplate.update("UPDATE events_users SET vote = ? WHERE eventid = ? AND userid = ? ",
				(isUpvote)? 1 : -1, eventid, userid);
	}

	@Override
	public int countUserInscriptionPages(final boolean onlyFuture, final long userid) {
		int rows = countByUserInscriptions(onlyFuture, userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}
	
	@Override
	public int countUserOwnedPages(final boolean onlyFuture, final long userid) {
		int rows = countByOwner(onlyFuture, userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public List<Event> findBy(Optional<String> eventName, Optional<String> clubName, Optional<String> sport,
			Optional<String> organizer, Optional<Integer> vacancies, Optional<Instant> date, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

}
