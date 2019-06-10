package ar.edu.itba.paw.persistence;

import java.math.BigInteger;
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
import javax.persistence.criteria.JoinType;
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
	
	private static final int MAX_ROWS = 10;
	private static final int MAX_EVENTS_PER_WEEK = 24 * 7;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Event> findByEventId(long eventid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		from.fetch("inscriptions", JoinType.LEFT);
		
		final TypedQuery<Event> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("eventid"), eventid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@Override
	public List<Event> findByOwner(boolean futureEvents, long userid, int pageNum) {
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT eventid FROM events "
				+ " WHERE userid = :userid AND starts_at ");
		idQueryString.append((futureEvents)? 
				" > :now ORDER BY starts_at ASC " : " <= :now ORDER BY starts_at DESC ");
		paramsMap.put("userid", userid);
		paramsMap.put("now", Timestamp.from(Instant.now()));
		
		final List<Long> ids = getPageIds(idQueryString.toString(), paramsMap, pageNum);
		
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
	public List<Event> findPastUserInscriptions(long userid, int pageNum) {
		StringBuilder idQueryString = new StringBuilder("SELECT eventid FROM events AS e "
				+ " WHERE EXISTS (SELECT eventid FROM events_users "
				+ " WHERE eventid = e.eventid AND userid = :userid) "
				+ " AND e.starts_at <= :now ORDER BY e.starts_at DESC");
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userid", userid);
		paramsMap.put("now", Timestamp.from(Instant.now()));
		
		final List<Long> ids = getPageIds(idQueryString.toString(), paramsMap, pageNum);
		
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
	public List<Event> findFutureUserInscriptions(long userid) {
		StringBuilder queryString = new StringBuilder("SELECT inscriptionEvent FROM Inscription AS i "
				+ " WHERE i.inscriptedUser.userid = :userid AND i.inscriptionEvent.startsAt > :now ");
		
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("userid", userid);
		query.setParameter("now", Instant.now());
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getResultList();
	}

	@Override
	public Integer countByUserInscriptions(boolean futureEvents, long userid) {
		StringBuilder queryString = new StringBuilder("SELECT count(i) FROM Inscription AS i WHERE "
				+ " i.inscriptedUser.userid = :userid AND i.inscriptionEvent.startsAt ");
		queryString.append((futureEvents) ? " > :now " : " <= :now ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("userid", userid);
		query.setParameter("now", Instant.now());
		
		return query.getSingleResult().intValue();
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
		/* Only for prevention */
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getResultList();
	}

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
		
		final List<Long> ids = getPageIds(idQueryString.toString(), paramsMap, pageNum);
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		from.fetch("inscriptions", JoinType.LEFT);
		final TypedQuery<Event> query = em.createQuery(
				cq.select(from).where(from.get("eventid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> getPageIds(String idQueryString, Map<String, Object> paramsMap, int pageNum) {
		Query idQuery = em.createNativeQuery(idQueryString);
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		return (List<Long>) idQuery.getResultList();
	}

	@Override
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies, 
			final Optional<Instant> date) {
		
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT count(eventid) ");
		idQueryString.append(getFilterQueryEndString(paramsMap, eventName, clubName, 
				sport, organizer, vacancies, date));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}

		return ((BigInteger) idQuery.getSingleResult()).intValue();
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
		
		StringBuilder queryString = new StringBuilder(" FROM events "
				+ " AS t ");
		
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
				+ " WHERE i.inscriptionEvent.eventid = :eventid";
		 
		TypedQuery<Long> query = em.createQuery(queryString, Long.class);
		query.setParameter("eventid", eventid);
		
		return query.getSingleResult().intValue();
	}

	@Override
	public Event create(String name, User owner, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt) throws EventOverlapException {
		
		String eventOverlapQueryString = "SELECT count(*) FROM Event AS e "
				+ " WHERE e.pitch.pitchid = :pitchid "
				+ " AND ((e.startsAt <= :startsAt AND e.endsAt > :startsAt) "
				+ " OR (e.startsAt > :startsAt AND e.startsAt < :endsAt))";
		
		TypedQuery<Long> query = em.createQuery(eventOverlapQueryString, Long.class);
		query.setParameter("pitchid", pitch.getPitchid());
		query.setParameter("startsAt", startsAt);
		query.setParameter("endsAt", endsAt);
		int eventOverlapQueryResult = query.getSingleResult().intValue();
		
		if(eventOverlapQueryResult > 0)
			throw new EventOverlapException("Pitch is already taken in the chosen time period");
		
		final Event event = new Event(name, owner, pitch, description, maxParticipants, startsAt, endsAt);
		em.persist(event);
		return event;
	}

	@Override
	public void joinEvent(User user, Event event) 
			throws UserAlreadyJoinedException, UserBusyException {
		
		String userBusyQueryString = "SELECT count(i) FROM Inscription AS i WHERE "
				+ " i.inscriptedUser.userid = :userid AND "
				+ " ((i.inscriptionEvent.startsAt <= :startsAt AND i.inscriptionEvent.endsAt > :startsAt) OR "
				+ " (i.inscriptionEvent.startsAt > :startsAt AND i.inscriptionEvent.startsAt < :endsAt))";
		
		TypedQuery<Long> query = em.createQuery(userBusyQueryString.toString(), Long.class);
		query.setParameter("userid", user.getUserid());
		query.setParameter("startsAt", event.getStartsAt());
		query.setParameter("endsAt", event.getEndsAt());
		
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