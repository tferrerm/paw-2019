package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class NewTournamentForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	private String name;
	
	@NotBlank
	@Pattern(regexp = "^[0-9]*[1-9][0-9]*")
	private String maxTeams;
	
	@NotBlank
	@Pattern(regexp = "^[0-9]*[1-9][0-9]*")
	private String teamSize;
	
	@NotBlank
	private String firstRoundDate;
	
	@NotBlank
	@Pattern(regexp = "[0-9]?[0-9]")
	private String startsAtHour;
	
	@NotBlank
	@Pattern(regexp = "[0-9]?[0-9]")
	private String endsAtHour;
	
	@NotBlank
	private String inscriptionEndDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaxTeams() {
		return maxTeams;
	}

	public void setMaxTeams(String maxTeams) {
		this.maxTeams = maxTeams;
	}

	public String getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(String teamSize) {
		this.teamSize = teamSize;
	}

	public String getFirstRoundDate() {
		return firstRoundDate;
	}

	public void setFirstRoundDate(String firstRoundDate) {
		this.firstRoundDate = firstRoundDate;
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

	public String getInscriptionEndDate() {
		return inscriptionEndDate;
	}

	public void setInscriptionEndDate(String inscriptionEndDate) {
		this.inscriptionEndDate = inscriptionEndDate;
	}
	

}
