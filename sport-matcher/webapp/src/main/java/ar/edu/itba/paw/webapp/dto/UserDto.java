package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

public class UserDto {
	
	private long userid;
	private String username;
	private String firstname;
	private String lastname;
	private int currentEventCount;

	public static UserDto ofUser(User user) {
		UserDto dto = new UserDto();
		dto.userid = user.getUserid();
		dto.username = user.getUsername();
		dto.firstname = user.getFirstname();
		dto.lastname = user.getLastname();

		return dto;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public int getCurrentEventCount() {
		return currentEventCount;
	}

	public void setCurrentEventCount(int currentEventCount) {
		this.currentEventCount = currentEventCount;
	}

}
