package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tournament_events")
public class TournamentEvent {
	
	@Id
	private Long eventid;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tournamentid")
	private Tournament tournament;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@MapsId("eventid")
	@JoinColumn(name = "eventid")
	private Event event;
	
	@Column(name = "round", nullable = false)
	private int round;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "first_teamid")
	private TournamentTeam firstTeam;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "second_teamid")
	private TournamentTeam secondTeam;
	
	@Column(name = "first_team_score", nullable = false)
	private Integer firstTeamScore;
	
	@Column(name = "second_team_score", nullable = false)
	private Integer secondTeamScore;
	
	/*package*/ TournamentEvent() {
		
	}
	
	public TournamentEvent(Tournament tournament, Event event, int round,
			TournamentTeam firstTeam, TournamentTeam secondTeam) {
		//this.eventid = event.getEventId(); // ?
		this.tournament = tournament;
		this.event = event;
		this.round = round;
		this.firstTeam = firstTeam;
		this.secondTeam = secondTeam;
		//this.firstTeamScore = null;
		//this.secondTeamScore = null;
	}
	
	public long getEventid() {
		return eventid;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public Event getEvent() {
		return event;
	}

	public int getRound() {
		return round;
	}

	public TournamentTeam getFirstTeam() {
		return firstTeam;
	}

	public TournamentTeam getSecondTeam() {
		return secondTeam;
	}

	public int getFirstTeamScore() {
		return firstTeamScore;
	}

	public int getSecondTeamScore() {
		return secondTeamScore;
	}
	
}
