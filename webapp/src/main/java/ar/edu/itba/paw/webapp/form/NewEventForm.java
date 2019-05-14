package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class NewEventForm {
	
	@NotBlank
	private String name;
	
	private String description;
	
	@NotBlank
	@Pattern(regexp = "[0-9]*")
	private String maxParticipants;
	
	@NotBlank
	private String date;
	
	@NotBlank
	@Pattern(regexp = "[0-9][0-9]")
	private String startsAtHour;
	
	@NotBlank
	@Pattern(regexp = "[0-9][0-9]")
	private String endsAtHour;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(String maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getStartsAtHour() {
		return startsAtHour;
	}

	public void setStartsAtHour(String startsAtHour) {
		this.startsAtHour = startsAtHour;
	}

	public String getEndsAtHour() {
		return endsAtHour;
	}

	public void setEndsAtHour(String endsAtHour) {
		this.endsAtHour = endsAtHour;
	}
	
}
