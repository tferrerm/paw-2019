package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class FormValidationException extends Exception {

	public FormValidationException(String msg) {
		super(msg);
	}
}
