package ar.edu.itba.paw.interfaces;

import java.util.Locale;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

public interface EmailService {
	
	public void userRegistered(final User user, final Locale locale);
	
	public void someoneJoinedYourEvent(final User joinedUser, final Event event, final Locale locale);

	public void youWereKicked(final User kickedUser, final Event event, final Locale locale);

	public void youWereKicked(final User kickedUser, final Tournament tournament, final Locale locale);

	public void tournamentStarted(final User user, final Event event, final Locale locale);
	
}
