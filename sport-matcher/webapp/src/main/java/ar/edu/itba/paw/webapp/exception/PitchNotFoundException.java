package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class PitchNotFoundException extends EntityNotFoundException {

	public PitchNotFoundException() {
		super("The requested pitch was not found");
	}

}
