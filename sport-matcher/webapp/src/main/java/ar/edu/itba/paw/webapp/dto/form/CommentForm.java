package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class CommentForm {
	
	@NotBlank
	@Size(max=500)
	private String comment;

	public String getComment() {
		return comment;
	}

	public CommentForm withComment(String comment) {
		this.comment = comment;
		return this;
	}

}
