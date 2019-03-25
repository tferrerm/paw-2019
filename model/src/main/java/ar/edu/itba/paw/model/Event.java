package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Event {
	private long eventId;
	private String name;
	private String location;
	private Instant startsAt;
	private Instant endsAt;
	private List<User> participants;
	private Instant createdAt;
	private Instant deletedAt;
	
	public Event(long eventId, String name, String location, Instant startsAt,
			Instant endsAt, List<User> participants) {
		this.eventId = eventId;
		this.name = name;
		this.location = location;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
		if(participants == null)
			this.participants = new ArrayList<User>();
		else
			this.participants = participants;
	}
	
	public Event(long eventId, String name, String location, Instant startsAt,
			Instant endsAt, List<User> participants, Instant createdAt, Instant deletedAt) {
		this(eventId, name, location, startsAt,
			endsAt, participants);
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}

}
