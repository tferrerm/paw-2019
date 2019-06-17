package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class DateInPastException extends Exception {

	public DateInPastException(String message) {
		super(message);
	}

}
