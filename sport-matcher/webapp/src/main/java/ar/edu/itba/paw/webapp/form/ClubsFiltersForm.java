package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ClubsFiltersForm {
	
	@Size(max=64)
	private String name;
	
	@Size(max=64)
	private String location;

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
	
}
