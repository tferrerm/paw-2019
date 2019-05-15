package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public interface EventService {
	
	public Optional<Event> findByEventId(final long eventid);
	
	public List<Event> findByUsername(final String username, int pageNum);
	
	public List<Event> findFutureEvents(int pageNum);
	
	public List<User> findEventUsers(final long eventid, final int pageNum);
	
	/**
	 * Finds events in a pitch. Only seven days of events will be returned.
	 * @param pitchid The id of the pitch
	 * @return a list of events (with a maximum size of 24 * 7)
	 */
	public List<Event> findCurrentEventsInPitch(final long pitchid);
	
	public List<Event> findBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<Sport> sport, Optional<Integer> vacancies, int page);
	
	public boolean[][] convertEventListToSchedule(List<Event> events, int minHour, 
			int maxHour, int dayAmount);
	
	public int countUserEventPages(final long userid);
	
	public int countFutureEventPages();
	
	public int countParticipants(long eventid);
	
	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final int maxParticipants, final Instant startsAt, final Instant endsAt);
	
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, EventFullException;
	
	public void leaveEvent(final User user, final Event event);
	
	public void kickFromEvent(final User owner, final long kickedUserId, final Event event)
			throws UserNotAuthorizedException;
	
	public void deleteEvent(long eventid);

	public String[] getScheduleDaysHeader();

}
