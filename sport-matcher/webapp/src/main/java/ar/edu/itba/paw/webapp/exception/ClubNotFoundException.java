package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class ClubNotFoundException extends EntityNotFoundException {

	public ClubNotFoundException() {
		super("The requested club was not found");
	}

}
