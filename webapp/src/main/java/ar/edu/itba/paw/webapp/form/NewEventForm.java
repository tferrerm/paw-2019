package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class NewEventForm {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String location;
	
	private String description;
	
	@NotBlank
	@Pattern(regexp = "[0-9]*")
	private String maxParticipants;
	
	@NotBlank
	private String startsAt;
	
	@NotBlank
	@Pattern(regexp = "[0-9]*")
	private String endsAt;


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

	public String getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(String startsAt) {
		this.startsAt = startsAt;
	}

	public String getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(String endsAt) {
		this.endsAt = endsAt;
	}
	
}
