package ar.edu.itba.paw.model;

import java.time.Instant;

public class Club {
	
	private long clubid;
	private String name;
	private String location;
	private Instant createdAt;
	
	public Club(long clubid, String name, String location, Instant createdAt) {
		this.clubid = clubid;
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
