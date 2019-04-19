package ar.edu.itba.paw.model;

import java.time.Instant;

public class Club {
	
	private long clubid;
	private User owner;
	private String name;
	private String location;
	private Instant createdAt;
	
	public Club(long clubid, User owner, String name, String location, Instant createdAt) {
		this.clubid = clubid;
		this.owner = owner;
		this.name = name;
		this.location = location;
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Club: " + name + "; id: " + clubid;
	}

	public long getClubid() {
		return clubid;
	}

	public User getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
