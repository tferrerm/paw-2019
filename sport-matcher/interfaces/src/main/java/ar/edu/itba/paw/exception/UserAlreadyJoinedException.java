package ar.edu.itba.paw.exception;

@SuppressWarnings("serial")
public class UserAlreadyJoinedException extends UserBusyException {
	
	public UserAlreadyJoinedException(long userid, long eventid) {
		super("User " + userid + " already joined event "
				+ eventid);
	}

}
