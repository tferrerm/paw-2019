package ar.edu.itba.paw.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Event;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventDao ed;

	@Override
	public Optional<Event> findByEventId(long eventid) {
		return ed.findByEventId(eventid);
	}

	@Override
	public List<Event> findByUsername(String username) {
		return ed.findByUsername(username);
	}
	
	@Override
	public List<Event> findFutureEvents() {
		return ed.findFutureEvents();
	}

	@Transactional
	@Override
	public Event create(String name, String location, String description, Instant startsAt, Instant endsAt) {
		return ed.create(name, location, description, startsAt, endsAt);
	}

}
