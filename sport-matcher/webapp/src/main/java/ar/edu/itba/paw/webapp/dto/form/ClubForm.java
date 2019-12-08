package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class ClubForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	@Size(max=100)
	private String name;

	@NotBlank
	@Size(max=500)
	private String location;

	public String getName() {
		return name;
	}

	public ClubForm withName(String name) {
		this.name = name;
		return this;
	}

	public String getLocation() { return location; }

	public ClubForm withLocation(String location) {
		this.location = location;
		return this;
	}
	
}
