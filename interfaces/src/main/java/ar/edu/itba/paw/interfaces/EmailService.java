package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface EmailService {
	
	public void joinEventEmail(String email, String eventOwnerName, String eventName,
							   final Locale locale);

}
