package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.Event;

public class EventDto {
	
	private long eventid;
	private String name;
	private UserDto owner;
	private String description;
	private int maxParticipants;
	private Instant inscriptionEnd;
	private boolean inscriptionSuccess;
	private Instant startsAt;
	private Instant endsAt;
	private Instant createdAt;
	
	public static EventDto ofEvent(Event event) {
		EventDto dto = new EventDto();
		dto.eventid = event.getEventId();
		dto.name = event.getName();
		dto.owner = UserDto.ofUser(event.getOwner());
		dto.maxParticipants = event.getMaxParticipants();
		dto.inscriptionEnd = event.getEndsInscriptionAt();
		dto.inscriptionSuccess = event.getInscriptionSuccess();
		dto.startsAt = event.getStartsAt();
		dto.endsAt = event.getEndsAt();
		dto.createdAt = event.getCreatedAt();
		
		return dto;
	}

	public long getEventid() {
		return eventid;
	}

	public void setEventid(long eventid) {
		this.eventid = eventid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserDto getOwner() {
		return owner;
	}

	public void setOwner(UserDto owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
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

	public Instant getInscriptionEnd() {
		return inscriptionEnd;
	}

	public void setInscriptionEnd(Instant inscriptionEnd) {
		this.inscriptionEnd = inscriptionEnd;
	}

	public boolean isInscriptionSuccess() {
		return inscriptionSuccess;
	}

	public void setInscriptionSuccess(boolean inscriptionSuccess) {
		this.inscriptionSuccess = inscriptionSuccess;
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
