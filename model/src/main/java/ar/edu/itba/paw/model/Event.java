package ar.edu.itba.paw.model;

import java.time.Instant;

public class Event {

	private long eventId;
	private String name;
	private User owner;
	private Pitch pitch;
	private String description;
	private int maxParticipants;
	private Instant startsAt;
	private Instant endsAt;
	private Instant createdAt;
	
	public Event(long eventId, String name, User owner, Pitch pitch, String description,
			int maxParticipants, Instant startsAt, Instant endsAt) {
		this.eventId = eventId;
		this.name = name;
		this.owner = owner;
		this.pitch = pitch;
		this.description = description;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	public Event(long eventId, String name, User owner, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt, Instant createdAt) {
		this(eventId, name, owner, pitch, description, maxParticipants, startsAt,
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

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Pitch getPitch() {
		return pitch;
	}
	
	public void setPitch(Pitch pitch) {
		this.pitch = pitch;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getMaxParticipants() {
		return maxParticipants;
	}
	
	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(Instant startsAt) {
		this.startsAt = startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(Instant endsAt) {
		this.endsAt = endsAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
