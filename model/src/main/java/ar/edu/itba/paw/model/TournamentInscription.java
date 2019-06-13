package ar.edu.itba.paw.model;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "tournament_teams_users")
public class TournamentInscription {
	
	@EmbeddedId
	private TournamentInscriptionId id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("teamid")
	@JoinColumn(name = "teamid", nullable = false)
	private TournamentTeam tournamentTeam;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("userid")
	@JoinColumn(name = "userid", nullable = false)
	private User inscriptedUser;
	
	/*package*/ TournamentInscription() {
		
	}
	
	public TournamentInscription(TournamentTeam tournamentTeam, User inscriptedUser) {
		this.tournamentTeam = tournamentTeam;
		this.inscriptedUser = inscriptedUser;
		this.id = new TournamentInscriptionId(tournamentTeam.getTeamId(), inscriptedUser.getUserid());
	}
	
	@Override
	public String toString() {
		return "Tournament Inscription: Team = " + tournamentTeam + " Userid = " + inscriptedUser;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof TournamentInscription))
			return false;
		TournamentInscription other = (TournamentInscription) o;
		return this.getTeam().equals(other.getTeam()) 
				&& this.getInscriptedUser().equals(other.getInscriptedUser());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTeam(), this.getInscriptedUser());
	}
	
	public TournamentTeam getTeam() {
		return tournamentTeam;
	}

	public User getInscriptedUser() {
		return inscriptedUser;
	}

}
