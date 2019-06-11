package ar.edu.itba.paw.model;

import java.util.List;

import javax.persistence.Entity;

//@Entity
public class Tournament {
	
	private String tournamentName;
	
	private int maxTeams;
	
	private List<Team> teams;

}
