package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class TournamentTeamInscriptionsCollectionDto {
	
	private List<TournamentTeamInscriptionsDto> teams;
	
	public static TournamentTeamInscriptionsCollectionDto ofTeams(List<TournamentTeamInscriptionsDto> teams) {
		TournamentTeamInscriptionsCollectionDto dto = new TournamentTeamInscriptionsCollectionDto();
		dto.teams = teams;
		
		return dto;
	}

	public List<TournamentTeamInscriptionsDto> getTeams() {
		return teams;
	}

	public void setTeams(List<TournamentTeamInscriptionsDto> teams) {
		this.teams = teams;
	}

}
