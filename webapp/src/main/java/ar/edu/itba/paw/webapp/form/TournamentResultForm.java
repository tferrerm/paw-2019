package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class TournamentResultForm {

	@NotBlank
	@Pattern(regexp = "^(0|[1-9][0-9]*)")
	private String firstResult;
	
	@NotBlank
	@Pattern(regexp = "^(0|[1-9][0-9]*)")
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
