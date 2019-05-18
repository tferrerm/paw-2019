package ar.edu.itba.paw.model;

import java.time.Instant;

public class Event {

	private long eventId;
	private String name;
	private User owner;
	private Pitch pitch;
	private String description;
	private int maxParticipants;
	private Long inscriptions;
	private Instant startsAt;
	private Instant endsAt;
	private Instant createdAt;
	
	public Event(long eventId, String name, Pitch pitch, String description,
			int maxParticipants, Instant startsAt, Instant endsAt) {
		this.eventId = eventId;
		this.name = name;
		this.pitch = pitch;
		this.description = description;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	public Event(long eventId, String name, User owner, Pitch pitch, String description,
	int maxParticipants, Instant startsAt, Instant endsAt) {
		this(eventId, name, pitch, description,
			maxParticipants, startsAt, endsAt);
		this.owner = owner;
	}
	
	public Event(long eventId, String name, User owner, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt, Instant createdAt) {
		this(eventId, name, owner, pitch, description, maxParticipants, startsAt,
			endsAt);
		this.createdAt = createdAt;
	}
	
	public Event(long eventId, String name, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt, Instant createdAt) {
		this(eventId, name, pitch, description, maxParticipants, startsAt,
			endsAt);
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Eventid: " + eventId + " Name: " + name;
	}
	
	public long getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public Pitch getPitch() {
		return pitch;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getMaxParticipants() {
		return maxParticipants;
	}
	
	/**
	 * Returns null unless setter is called.
	 */
	public Long getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(Long inscriptions) {
		this.inscriptions = inscriptions;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
