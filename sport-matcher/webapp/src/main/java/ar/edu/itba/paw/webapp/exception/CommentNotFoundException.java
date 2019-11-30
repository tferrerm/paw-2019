package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class CommentNotFoundException extends EntityNotFoundException {

	public CommentNotFoundException() {
		super("The requested comment was not found");
	}

}
