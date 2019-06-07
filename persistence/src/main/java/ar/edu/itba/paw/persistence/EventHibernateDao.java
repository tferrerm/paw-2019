package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Repository
public class EventHibernateDao implements EventDao {
	
	private static final int MAX_ROWS = 10;
	private static final int MAX_EVENTS_PER_WEEK = 24 * 7;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Event> findByEventId(long eventid) {
		return Optional.of(em.find(Event.class, eventid));
	}

	@Override
	public List<Event> findByOwner(boolean futureEvents, long userid, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByOwner(boolean futureEvents, long userid) {
		StringBuilder queryString = new StringBuilder("SELECT count(*) FROM Event AS e "
				+ " WHERE e.userid = :userid AND e.startsAt ");
		queryString.append((futureEvents) ? " > :now " : " <= :now ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("now", Instant.now());
		query.setParameter("userid", userid);
		
		return query.getSingleResult().intValue();
	}

	@Override
	public List<Event> findFutureUserInscriptions(long userid) {
		StringBuilder queryString = new StringBuilder("SELECT e FROM Event AS e JOIN e.participants AS u "
				+ " WHERE u.userid = :userid AND e.startsAt > :now ORDER BY e.startsAt ASC");
		
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("now", Timestamp.from(Instant.now()));
		query.setParameter("userid", userid);
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> findPastUserInscriptions(long userid, int pageNum) {
		Query idQuery = em.createNativeQuery("SELECT eventid FROM events ");
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Event> cq = cb.createQuery(Event.class);
		Root<Event> from = cq.from(Event.class);
		
		final TypedQuery<Event> query = em.createQuery(
				cq.select(from).where(from.get("eventid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
		
		/*StringBuilder query = new StringBuilder("SELECT * FROM (events NATURAL JOIN pitches "
				+ " NATURAL JOIN users NATURAL JOIN clubs) AS t "
				+ " WHERE EXISTS (SELECT eventid FROM events_users "
				+ " WHERE eventid = t.eventid AND userid = ?) AND t.starts_at "
				+ " <= ? ORDER BY t.starts_at DESC LIMIT ? OFFSET ?");*/
		
	}

	@Override
	public Integer countByUserInscriptions(boolean futureEvents, long userid) {
		/*StringBuilder queryString = new StringBuilder("SELECT count(*) "
				+ " FROM Event AS e JOIN e.participants AS u "
				+ " WHERE u.userid = :userid AND e.starts_at > :now ORDER BY e.starts_at ASC");
		
		TypedQuery<Event> query = em.createQuery(queryString.toString(), Event.class);
		query.setParameter("now", Instant.now());
		query.setParameter("userid", userid);
		query.setMaxResults(MAX_EVENTS_PER_WEEK);
		
		return query.getResultList();*/
		return null;
	}

	@Override
	public List<Event> findFutureEvents(int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findEventUsers(long eventid, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Event> findCurrentEventsInPitch(long pitchid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Event> findBy(boolean onlyFuture, Optional<String> eventName, Optional<String> establishment,
			Optional<String> sport, Optional<String> organizer, Optional<Integer> vacancies, Optional<Instant> date,
			int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countFilteredEvents(boolean onlyFuture, Optional<String> eventName, Optional<String> clubName,
			Optional<String> sport, Optional<String> organizer, Optional<Integer> vacancies, Optional<Instant> date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long[]> countBy(boolean onlyFuture, Optional<String> eventName, Optional<String> establishment,
			Optional<String> sport, Optional<String> organizer, Optional<Integer> vacancies, Optional<Instant> date,
			int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countUserEventPages(long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countFutureEventPages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countParticipants(long eventid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Event create(String name, User owner, Pitch pitch, String description, int maxParticipants, Instant startsAt,
			Instant endsAt) throws EventOverlapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void joinEvent(User user, Event event) throws UserAlreadyJoinedException, UserBusyException {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countUserOwnedPages(boolean onlyFuture, long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

}
