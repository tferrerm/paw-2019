package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class EventNotFoundException extends EntityNotFoundException {

	public EventNotFoundException() {
		super("The requested event was not found");
	}

}
