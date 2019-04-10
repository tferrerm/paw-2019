package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.Instant;

public class Event {
	private long eventId;
	private String name;
	private String location;
	private String description;
	private Instant startsAt;
	private Instant endsAt;
	private Instant createdAt;
	private Instant deletedAt;
	
	public Event(long eventId, String name, String location, String description, Instant startsAt,
			Instant endsAt) {
		this.eventId = eventId;
		this.name = name;
		this.location = location;
		this.description = description;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	public Event(long eventId, String name, String location, String description, Instant startsAt,
			Instant endsAt, Instant createdAt, Instant deletedAt) {
		this(eventId, name, location, description, startsAt,
			endsAt);
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}
	
	public Event(long eventId, String name, String location, String description, Timestamp startsAt,
			Timestamp endsAt, Timestamp createdAt, Timestamp deletedAt) {
		this.eventId = eventId;
		this.name = name;
		this.location = location;
		this.description = description;
		this.startsAt = startsAt.toInstant();
		this.endsAt = endsAt.toInstant();
		this.createdAt = createdAt.toInstant();
		if(deletedAt != null)
			this.deletedAt = deletedAt.toInstant();
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

}
