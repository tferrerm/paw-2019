package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Event;

public interface EventService {
	
	public Optional<Event> findByEventId(final long eventid);
	
	public List<Event> findByUsername(final String username, int pageNum);
	
	public List<Event> findFutureEvents(int pageNum);
	
	public long countUserEventPages(final long userid);
	
	public long countFutureEventPages();
	
	public Event create(final String name, final String location, final String description,
			final Instant startsAt, final Instant endsAt);

}
