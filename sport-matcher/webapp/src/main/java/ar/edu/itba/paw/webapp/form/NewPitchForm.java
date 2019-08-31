package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class NewPitchForm {
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9 ]+")
	@Size(max=100)
	private String name;
	
	@NotBlank
	@Size(max=100)
	private String sport;
	
	private MultipartFile pitchPicture;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}
	
	public MultipartFile getPitchPicture() {
		return pitchPicture;
	}

	public void setPitchPicture(MultipartFile pitchPicture) {
		this.pitchPicture = pitchPicture;
	}
	
}