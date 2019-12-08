package ar.edu.itba.paw.webapp.dto.form;

import java.time.Instant;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ar.edu.itba.paw.webapp.dto.form.validator.FutureDate;
import ar.edu.itba.paw.webapp.dto.form.validator.IntegerRange;

public class TournamentForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	@Size(max=100)
	private String name;
	
	@IntegerRange(min = 2, max = 4)
	private Integer maxTeams;

	@IntegerRange(min = 1, max = 11)
	private Integer teamSize;
	
	@FutureDate(startOfDay = true)
	private Instant firstRoundDate;
	
	@IntegerRange(min = 0, max = 23)
	private Integer startsAtHour;
	
	@IntegerRange(min = 0, max = 23)
	private Integer endsAtHour;
	
	@FutureDate
	private Instant inscriptionEndDate;

	public String getName() {
		return name;
	}

	public TournamentForm withName(String name) {
		this.name = name;
		return this;
	}

	public Integer getMaxTeams() {
		return maxTeams;
	}

	public TournamentForm withMaxTeams(Integer maxTeams) {
		this.maxTeams = maxTeams;
		return this;
	}

	public Integer getTeamSize() {
		return teamSize;
	}

	public TournamentForm withTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
		return this;
	}

	public Instant getFirstRoundDate() {
		return firstRoundDate;
	}

	public TournamentForm withtFirstRoundDate(Instant firstRoundDate) {
		this.firstRoundDate = firstRoundDate;
		return this;
	}

	public Integer getStartsAtHour() {
		return startsAtHour;
	}

	public TournamentForm withStartsAtHour(Integer startsAtHour) {
		this.startsAtHour = startsAtHour;
		return this;
	}

	public Integer getEndsAtHour() {
		return endsAtHour;
	}

	public TournamentForm withEndsAtHour(Integer endsAtHour) {
		this.endsAtHour = endsAtHour;
		return this;
	}

	public Instant getInscriptionEndDate() {
		return inscriptionEndDate;
	}

	public TournamentForm withInscriptionEndDate(Instant inscriptionEndDate) {
		this.inscriptionEndDate = inscriptionEndDate;
		return this;
	}

}
