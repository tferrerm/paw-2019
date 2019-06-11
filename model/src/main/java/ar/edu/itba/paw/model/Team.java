package ar.edu.itba.paw.model;

import java.util.List;

import javax.persistence.Entity;

//@Entity
public class Team {
	
	private String teamName;
	
	private List<User> members;
	
	private Tournament tournament;
	
	
	
}
