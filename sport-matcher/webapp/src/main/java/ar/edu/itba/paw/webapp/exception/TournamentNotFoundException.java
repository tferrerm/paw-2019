package ar.edu.itba.paw.webapp.exception;

@SuppressWarnings("serial")
public class TournamentNotFoundException extends EntityNotFoundException {

	public TournamentNotFoundException() {
		super("The requested tournament was not found");
	}

}
