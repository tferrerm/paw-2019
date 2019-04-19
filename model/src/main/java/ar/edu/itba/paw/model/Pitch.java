package ar.edu.itba.paw.model;

import java.time.Instant;

public class Pitch {
	
	private long pitchid;
	private Club club;
	private String name;
	private String sport;
	private Instant createdAt;
	
	public Pitch(long pitchid, Club club, String name, String sport, Instant createdAt) {
		this.pitchid = pitchid;
		this.club = club;
		this.name = name;
		this.sport = sport;
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "Pitch: " + name + "; id: " + pitchid;
	}

	public long getPitchid() {
		return pitchid;
	}

	public Club getClub() {
		return club;
	}

	public String getName() {
		return name;
	}

	public String getSport() {
		return sport;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
