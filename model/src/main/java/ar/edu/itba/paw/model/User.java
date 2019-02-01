package ar.edu.itba.paw.model;

import java.sql.Timestamp;
import java.time.Instant;

public class User {
	
	private long userid;
	private String username;
	private String password;
	private Role role;
	private Instant createdAt;
	private Instant deletedAt;
	
	public User(long userid, String username, String password, Role role,
			Instant createdAt, Instant deletedAt) {
		this(userid, username, password, role);
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
	}
	
	public User(long userid, String username, String password, Role role) {
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public User(long userid, String username, String password, Role role,
			Timestamp createdAt, Timestamp deletedAt) {
		this(userid, username, password, role);
		this.createdAt = createdAt.toInstant();
		if(deletedAt != null)
			this.deletedAt = deletedAt.toInstant();
	}

	public long getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public Role getRole() {
		return role;
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public Instant getDeletedAt() {
		return deletedAt;
	}

}
