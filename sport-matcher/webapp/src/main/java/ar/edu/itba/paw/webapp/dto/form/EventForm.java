package ar.edu.itba.paw.webapp.dto.form;

import java.time.Instant;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ar.edu.itba.paw.webapp.dto.form.validator.FutureDate;
import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class EventForm {
	
	@NotBlank
	@Size(max = 100)
	private String name;
	
	@Size(max = 500)
	private String description;

	@IntegerRange(min = 1, max = 100)
	private Integer maxParticipants;
	
	@FutureDate(startOfDay = true)
	private Instant date;

	@IntegerRange(min = 0, max = 23)
	private Integer startsAtHour;

	@IntegerRange(min = 0, max = 23)
	private Integer endsAtHour;
	
	@FutureDate
	private Instant inscriptionEndDate;

	public String getName() {
		return name;
	}

	public EventForm withName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EventForm withDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public EventForm withMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
		return this;
	}

	public Instant getDate() {
		return date;
	}

	public EventForm withDate(Instant date) {
		this.date = date;
		return this;
	}

	public Integer getStartsAtHour() {
		return startsAtHour;
	}

	public EventForm withStartsAtHour(Integer startsAtHour) {
		this.startsAtHour = startsAtHour;
		return this;
	}

	public Integer getEndsAtHour() {
		return endsAtHour;
	}

	public EventForm withEndsAtHour(Integer endsAtHour) {
		this.endsAtHour = endsAtHour;
		return this;
	}

	public Instant getInscriptionEndDate() {
		return inscriptionEndDate;
	}

	public EventForm withInscriptionEndDate(Instant inscriptionEndDate) {
		this.inscriptionEndDate = inscriptionEndDate;
		return this;
	}
	
}
