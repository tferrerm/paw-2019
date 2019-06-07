package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public class EventHibernateDao implements EventDao {

	@Override
	public Optional<Event> findByEventId(long eventid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Event> findByOwner(boolean futureEvents, long userid, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByOwner(boolean futureEvents, long userid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Event> findFutureUserInscriptions(long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Event> findPastUserInscriptions(long userid, int pageNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countByUserInscriptions(boolean futureEvents, long userid) {
		// TODO Auto-generated method stub
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
		return null;
	}

	@Override
	public Optional<Club> getFavoriteClub(long userid) {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public Optional<Integer> getUserVote(long eventid, long userid) {
		// TODO Auto-generated method stub
		return null;
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
