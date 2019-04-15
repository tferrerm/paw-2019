package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserAlreadyJoinedException extends Exception {
	
	public UserAlreadyJoinedException(String message) {
		super(message);
	}

}
