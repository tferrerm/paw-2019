package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Repository
public class EventHibernateDao implements EventDao {
	
	private static final int MAX_ROWS = 1;
	private static final int MAX_EVENTS_PER_WEEK = 24 * 7;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Event> findByEventId(long eventid) {
		return Optional.of(em.find(Event.class, eventid));
		
	}

	@Override
	public List<Event> findByOwner(boolean futureEvents, long userid, int pageNum) {
		StringBuilder queryString = new StringBuilder("FROM Event AS e "
				+ " WHERE e.owner.userid = :userid AND e.startsAt ");
		queryString.append((futureEvents)? 
				" > :now ORDER BY e.startsAt ASC " : " <= :now ORDER BY e.startsAt DESC ");
		
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("now", Instant.now());
		query.setParameter("userid", userid);
		query.setFirstResult((pageNum - 1) * MAX_ROWS);
		query.setMaxResults(MAX_ROWS);
		
		return query.getResultList();
	}

	@Override
	public int countByOwner(boolean futureEvents, long userid) {
		StringBuilder queryString = new StringBuilder("SELECT count(*) FROM Event AS e "
				+ " WHERE e.owner.userid = :userid AND e.startsAt ");
		queryString.append((futureEvents) ? " > :now " : " <= :now ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("now", Instant.now());
		query.setParameter("userid", userid);
		
		return query.getSingleResult().intValue();
	}

	@Override
	public List<Event> findByUserInscriptions(boolean futureEvents, long userid, int pageNum) {
		StringBuilder queryString = new StringBuilder("SELECT i.event FROM Inscription AS i "
				+ " WHERE i.inscriptedUser.userid = :userid AND i.event.startsAt ");
		queryString.append((futureEvents) ? " > :now " : " <= :now ");
		queryString.append(" ORDER BY i.event.startsAt ASC");
		 
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("userid", userid);
		query.setParameter("now", Instant.now());
		if(futureEvents) {
			query.setMaxResults(MAX_EVENTS_PER_WEEK);
		} else {
			query.setFirstResult((pageNum - 1) * MAX_ROWS);
			query.setMaxResults(MAX_ROWS);
		}
		
		return query.getResultList();
	}

	@Override
	public Integer countByUserInscriptions(boolean futureEvents, long userid) {
		StringBuilder queryString = new StringBuilder("SELECT count(i) FROM Inscription AS i WHERE "
				+ " i.inscriptedUser.userid = :userid AND i.event.startsAt ");
		queryString.append((futureEvents) ? " > :now " : " <= :now ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("userid", userid);
		query.setParameter("now", Instant.now());
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getSingleResult().intValue();
	}

	@Override
	public List<Event> findFutureEvents(int pageNum) { // SOLO LO USA TEST
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findEventUsers(long eventid, int pageNum/*SACAR?*/) {
		String queryString = "SELECT i.inscriptedUser FROM Inscription AS i "
				+ " WHERE i.event.eventid = :eventid";
		 
		TypedQuery<User> query = em.createQuery(queryString, User.class);
		query.setParameter("eventid", eventid);
		
		return query.getResultList();
	}

	@Override
	public List<Event> findCurrentEventsInPitch(long pitchid) {
		LocalDate ld = LocalDate.now();
		// Today at 00:00
		Instant today = ld.atStartOfDay().atZone(ZoneId.of(TIME_ZONE)).toInstant();
		// In seven days at 23:00
		Instant inAWeek = today.plus(8, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS);
		
		String queryString = "FROM Event AS e WHERE e.pitch.pitchid = :pitchid AND "
				+ " e.startsAt > :today AND e.startsAt < :inAWeek";
		
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("pitchid", pitchid);
		query.setParameter("today", today);
		query.setParameter("inAWeek", inAWeek);
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> findBy(final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies, 
			final Optional<Instant> date, final int pageNum) {
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT eventid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, eventName, clubName, 
				sport, organizer, vacancies, date));
		idQueryString.append(" ORDER BY t.starts_at ASC, t.eventid ASC ");
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		
		final TypedQuery<Event> query = em.createQuery(
				cq.select(from).where(from.get("eventid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies, 
			final Optional<Instant> date) {
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT eventid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, eventName, clubName, 
				sport, organizer, vacancies, date));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}

		return idQuery.getResultList().size();
	}
	
	private String getFilterQueryEndString(Map<String, Object> paramsMap,
			final Optional<String> eventName, final Optional<String> clubName, 
			final Optional<String> sport, final Optional<String> organizer, 
			final Optional<Integer> vacancies, final Optional<Instant> date) {
		
		Filter[] params = { 
				new Filter("eventname", eventName),
				new Filter("clubname", clubName),
				new Filter("sport", sport),
				new Filter("firstname || ' ' || lastname", organizer),
				new Filter("customVacanciesFilter", vacancies),
				new Filter("starts_at", Optional.of(
						(date.isPresent()) ? Timestamp.from(date.get()) : Timestamp.from(Instant.now())
				))
		};
		
		StringBuilder queryString = new StringBuilder(" FROM (events NATURAL JOIN pitches "
				+ " NATURAL JOIN clubs NATURAL JOIN users) AS t ");
		
		for(Filter param : params) {
			if(param.getValue().isPresent()) {
				int paramNum = paramsMap.size();
				switch(param.getName()) {
				case "customVacanciesFilter":
					queryString.append(buildPrefix(paramNum));
					queryString.append(" :" + Filter.getParamName() + paramNum + 
							" <= max_participants - (SELECT count(*) FROM events_users "
							+ " WHERE eventid = t.eventid) ");
					break;
				case "starts_at":
					queryString.append(buildPrefix(paramNum));
					queryString.append(param.queryAsDateRange(paramNum, date.isPresent()));
					break;
				default:
					if(isEmpty(param.getValue()))
						continue;
					queryString.append(buildPrefix(paramNum));
					queryString.append(param.queryAsString(paramNum));
					break;
				}
				paramsMap.put(Filter.getParamName() + paramNum, param.getValue().get());
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
	public int countUserEventPages(long userid) { // NO SE USA?
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countFutureEventPages() {
		String queryString = "SELECT count(e) FROM Event AS e "
				+ " WHERE e.startsAt > :now";
		 
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("now", Instant.now());
		
		int rows = query.getSingleResult().intValue();
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		
		return pageCount;
	}

	@Override
	public int countParticipants(long eventid) {
		String queryString = "SELECT count(i) FROM Inscription AS i "
				+ " WHERE i.event.eventid = :eventid";
		 
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("eventid", eventid);
		
		return query.getSingleResult().intValue();
	}

	@Override
	public Event create(String name, User owner, Pitch pitch, String description, int maxParticipants, Instant startsAt,
			Instant endsAt) throws EventOverlapException {
		final Event event = new Event(name, owner, pitch, description, 
				maxParticipants, startsAt, endsAt);
		em.persist(event);
		return event;
	}

	@Override
	public void joinEvent(User user, Event event) 
			throws UserAlreadyJoinedException, UserBusyException {
		
		Timestamp eventStartsAt = Timestamp.from(event.getStartsAt());
		Timestamp eventEndsAt = Timestamp.from(event.getEndsAt());
		
		String userBusyQueryString = "SELECT count(i) FROM Inscription AS i WHERE "
				+ " i.inscriptedUser.userid = :userid AND "
				+ " ((i.event.startsAt <= :startsAt AND ends_at > :startsAt) OR "
				+ " (starts_at > :startsAt AND starts_at < :endsAt))";
		
		TypedQuery<Long> query = em.createQuery(userBusyQueryString.toString(), Long.class);
		query.setParameter("userid", user.getUserid());
		query.setParameter("startsAt", event.getStartsAt()); // ARREGLAR
		query.setParameter("endsAt", event.getEndsAt()); // ARREGLAR
		
		int userBusyQueryResult = query.getSingleResult().intValue();
		
		if(userBusyQueryResult > 0)
			throw new UserBusyException("User " + user.getUserid() + " already joined "
					+ "an event in that period");
		
		try {
			em.persist(new Inscription(event, user));
		} catch(EntityExistsException e) {
			throw new UserAlreadyJoinedException("User " + user.getUserid() + " already joined event "
					+ event.getEventId());
		}
	}

	@Override
	public void leaveEvent(User user, Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public int kickFromEvent(long kickedUserId, long eventId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countUserEvents(boolean isCurrentEventsQuery, long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countUserOwnedCurrEvents(long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<Sport> getFavoriteSport(long userid) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Club> getFavoriteClub(long userid) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public int getPageInitialEventIndex(int pageNum) {
		return (pageNum - 1) * MAX_ROWS + 1;
	}

	@Override
	public void deleteEvent(long eventid) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Integer> getVoteBalance(long eventid) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Integer> getUserVote(long eventid, long userid) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public int vote(boolean isUpvote, long eventid, long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countUserInscriptionPages(boolean onlyFuture, long userid) {
		int rows = countByUserInscriptions(onlyFuture, userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public int countUserOwnedPages(boolean onlyFuture, long userid) {
		int rows = countByOwner(onlyFuture, userid);
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

}
