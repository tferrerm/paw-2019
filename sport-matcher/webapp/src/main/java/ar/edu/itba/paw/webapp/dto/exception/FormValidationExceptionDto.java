package ar.edu.itba.paw.webapp.dto.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

import ar.edu.itba.paw.webapp.exception.FormValidationException;

public class FormValidationExceptionDto {
	
	private String message;
	private Set<? extends ConstraintViolation<?>> constraintViolations;
	
	public static FormValidationExceptionDto ofException(FormValidationException e) {
		FormValidationExceptionDto dto = new FormValidationExceptionDto();
		dto.message = e.getMessage();
		dto.constraintViolations = e.getConstraintViolations();
		
		return dto;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<? extends ConstraintViolation<?>> getConstraintViolations() {
		return constraintViolations;
	}

	public void setConstraintViolations(Set<? extends ConstraintViolation<?>> constraintViolations) {
		this.constraintViolations = constraintViolations;
	}

}
