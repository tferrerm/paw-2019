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
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Club))
			return false;
		Club other = (Club) o;
		return clubid == other.getClubid() && name.equals(other.getName()) 
				&& location.equals(other.getLocation());
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
