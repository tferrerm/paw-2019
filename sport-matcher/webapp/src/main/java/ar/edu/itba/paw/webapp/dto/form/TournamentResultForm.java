package ar.edu.itba.paw.webapp.dto.form;

import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class TournamentResultForm {

	@IntegerRange
	private Integer firstResult;
	
	@IntegerRange
	private Integer secondResult;

	public Integer getFirstResult() {
		return firstResult;
	}

	public TournamentResultForm withFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public Integer getSecondResult() {
		return secondResult;
	}

	public TournamentResultForm withSecondResult(Integer secondResult) {
		this.secondResult = secondResult;
		return this;
	}
	
}
