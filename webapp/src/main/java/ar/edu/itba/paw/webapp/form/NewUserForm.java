package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class NewUserForm {

	@NotBlank
	@Size(min = 5, max = 100)
	@Pattern(regexp = "[a-zA-Z0-9\\s]*")
	private String username;

	@Size(min = 6, max = 100)
	private String password;

	@Size(min = 6, max = 100)
	private String repeatPassword;
	
	// private MultipartFile profilePicture;

	public boolean repeatPasswordMatching() {
		return password.equals(repeatPassword);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
//	public MultipartFile getProfilePicture() {
//		return profilePicture;
//	}
//
//	public void setProfilePicture(MultipartFile profilePicture) {
//		this.profilePicture = profilePicture;
//	}

}
