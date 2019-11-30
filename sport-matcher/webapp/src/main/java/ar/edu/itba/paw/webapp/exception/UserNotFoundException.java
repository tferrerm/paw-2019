package ar.edu.itba.paw.webapp.exception;

import ar.edu.itba.paw.exception.EntityNotFoundException;

@SuppressWarnings("serial")
public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException() {
		super("The requested user was not found.");
	}

}
