package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EndsBeforeStartsException extends EventCreationException {
	
	public EndsBeforeStartsException() {
		super("The event's end date must be greater to the event's start date");
	}

}
