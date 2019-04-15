package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;

public interface EventService {
	
	public Optional<Event> findByEventId(final long eventid);
	
	public List<Event> findByUsername(final String username, int pageNum);
	
	public List<Event> findFutureEvents(int pageNum);
	
	public List<User> findEventUsers(final long eventid, final int pageNum);
	
	public int countUserEventPages(final long userid);
	
	public int countFutureEventPages();
	
	public int countParticipants(long eventid);
	
	public Event create(final String name, final User owner, final String location, final String description,
			final int maxParticipants, final Instant startsAt, final Instant endsAt);
	
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, EventFullException;

}
