package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UserForm {

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
	private String username;

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z]+")
	@Size(max = 100)
	private String firstname;

	@NotBlank
	@Pattern(regexp = "[a-zA-Z]+")
	@Size(max = 100)
	private String lastname;
	
	//private MultipartFile profilePicture;

	public String getUsername() {
		return username;
	}

	public UserForm withUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}
	public UserForm withPassword(String password) {
		this.password = password;
		return this;
	}

	public String getFirstname() { return firstname; }

	public UserForm withFirstname(String firstname) {
		this.firstname = firstname;
		return this;
	}

	public String getLastname() { return lastname; }

	public UserForm withLastname(String lastname) {
		this.lastname = lastname;
		return this;
	}

//	public MultipartFile getProfilePicture() {
//		return profilePicture;
//	}
//
//	public void setProfilePicture(MultipartFile profilePicture) {
//		this.profilePicture = profilePicture;
//	}

}
