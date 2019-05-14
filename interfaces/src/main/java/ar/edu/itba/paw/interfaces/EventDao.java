package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.User;

public interface EventDao {
	
	public Optional<Event> findByEventId(final long eventid);
	
	public List<Event> findByUsername(final String username, final int pageNum);
	
	public List<Event> findFutureEvents(final int pageNum);
	
	public List<User> findEventUsers(final long eventid, final int pageNum);
	
	/**
	 * Finds events in a pitch. Only seven days of events will be returned.
	 * @param pitchid The id of the pitch
	 * @return a list of events (with a maximum size of 24 * 7)
	 */
	public List<Event> findCurrentEventsInPitch(final long pitchid);
	
	public int countUserEventPages(final long userid);
	
	public int countFutureEventPages();
	
	public int countParticipants(long eventid);
	
	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final int maxParticipants, final Instant startsAt, final Instant endsAt);
	
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException;
	
	public void leaveEvent(final User user, final Event event);
	
	public void deleteEvent(long eventid);

}
