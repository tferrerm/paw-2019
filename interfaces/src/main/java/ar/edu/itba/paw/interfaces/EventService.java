package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;

public interface EventService {
	
	public Optional<Event> findByEventId(final long eventid);
	
	public List<Event> findByUsername(final String username, int pageNum);
	
	public List<Event> findFutureEvents(int pageNum);
	
	public int countUserEventPages(final long userid);
	
	public int countFutureEventPages();
	
	public Event create(final String name, final User owner, final String location, final String description,
			final Instant startsAt, final Instant endsAt);

}
