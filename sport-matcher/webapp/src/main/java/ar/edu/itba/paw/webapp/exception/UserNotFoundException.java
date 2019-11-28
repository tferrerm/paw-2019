package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException() {
		super("The requested user was not found.");
	}

}
