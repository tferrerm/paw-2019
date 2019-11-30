package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class EventFullException extends InscriptionClosedException {

	public EventFullException() {
		super("Cannot join an event that has reached its maximum participants");
	}

}
