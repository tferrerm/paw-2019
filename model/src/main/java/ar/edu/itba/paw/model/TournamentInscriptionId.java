package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class TournamentInscriptionId implements Serializable {

	@Column(name = "teamid", nullable = false)
	private Long teamid;
	
	@Column(name = "userid", nullable = false)
	private Long userid;
	
	/*package*/ TournamentInscriptionId() {
		
	}
	
	public TournamentInscriptionId(Long teamid, Long userid) {
		this.teamid = teamid;
		this.userid = userid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof TournamentInscriptionId))
			return false;
		TournamentInscriptionId other = (TournamentInscriptionId) o;
		return this.getTeamid().equals(other.getTeamid()) 
				&& this.getUserid().equals(other.getUserid());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getTeamid(), this.getUserid());
	}

	public Long getTeamid() {
		return teamid;
	}

	public Long getUserid() {
		return userid;
	}
	
}
