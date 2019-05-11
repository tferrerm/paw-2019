package ar.edu.itba.paw.model;

import java.time.Instant;

public class User {
	
	private long userid;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private Role role;
	private Instant createdAt;
	
	public User(long userid, String username, String firstname, String lastname, 
			String password, Role role, Instant createdAt) {
		this(userid, username, firstname, lastname, password, role);
		this.createdAt = createdAt;
	}
	
	public User(long userid, String username, String firstname, String lastname, 
			String password, Role role) {
		this.userid = userid;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "Userid: " + userid + " Username: " + username;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof User))
			return false;
		User other = (User) o;
		return this.getUsername().equals(other.getUsername());
	}
	
	@Override
	public int hashCode() {
		return this.getUsername().hashCode() * 83;
	}

	public long getUserid() {
		return userid;
	}

	public String getUsername() {
		return username;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
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

}
