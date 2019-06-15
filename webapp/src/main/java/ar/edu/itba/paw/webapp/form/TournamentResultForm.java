package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class TournamentResultForm {

	@NotBlank
	@Size(max=3)
	private String firstResult;
	
	@NotBlank
	@Size(max=3)
	private String secondResult;

	public String getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(String firstResult) {
		this.firstResult = firstResult;
	}

	public String getSecondResult() {
		return secondResult;
	}

	public void setSecondResult(String secondResult) {
		this.secondResult = secondResult;
	}
	
}
