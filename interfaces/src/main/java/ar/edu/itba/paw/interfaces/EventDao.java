package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public interface EventDao {

	public Optional<Event> findByEventId(final long eventid);

	/**
	 * Gets Events owned by a User.
	 * @param futureEvents 	Finds only future Events (true) or only past Events (false).
	 * @param username 		Owner of Events.
	 * @param pageNum 		Page number.
	 * @return 				List of Events.
	 */
	public List<Event> findByUsername(boolean futureEvents, final String username, final int pageNum);

	public List<Event> findByOwner(boolean futureEvents, final String username, final int pageNum);

	public List<Event> findFutureEvents(final int pageNum);

	public List<User> findEventUsers(final long eventid, final int pageNum);

	/**
	 * Finds Events in a Pitch. Only seven days of events will be returned.
	 * @param pitchid 	The id of the Pitch.
	 * @return a list of Events (with a maximum size of 24 * 7).
	 */
	public List<Event> findCurrentEventsInPitch(final long pitchid);

	/**
	 * Returns a list of Events matching present filters.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param name				String to match an Event's name with.
	 * @param establishment		String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param page				Page number.
	 * @return
	 */
	public List<Event> findBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<String> sport, Optional<Integer> vacancies, int page);

	/**
	 * Returns a combination of eventid and vacancies for that Event.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param name				String to match an Event's name with.
	 * @param establishment		String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param page				Page number.
	 * @return
	 */
	public List<Long[]> countBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<String> sport, Optional<Integer> vacancies, int page);

	public int countUserEventPages(final long userid);

	public int countFutureEventPages();

	/**
	 * Gets the amount of Users currently participating in an Event.
	 * @param	eventid		The Event's id
	 * @return the amount of Users currently participating in the Event.
	 */
	public int countParticipants(final long eventid);

	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final int maxParticipants, final Instant startsAt, final Instant endsAt);

	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, UserBusyException;

	public void leaveEvent(final User user, final Event event);

	public int kickFromEvent(final long kickedUserId, final long eventId);

	/**
	 * Gets the amount of current or past Events a User has participated in.
	 * @param	isCurrentEventsQuery	Search for current Events only (true) or past Events only (false).
	 * @param	userid 					The User's id.
	 * @return							The amount of current Events owned by the User.
	 */
	public int countUserEvents(boolean isCurrentEventsQuery, final long userid);

	/**
	 * Gets the amount of current (not finished) Events owned by a User.
	 * @param	userid 	The User's id.
	 * @return the amount of current Events owned by the User.
	 */
	public int countUserOwnedCurrEvents(final long userid);

	/**
	 * Returns a User's favorite Sport(s) based on Events joined.
	 * @param 	userid 	The User's id.
	 * @return the User's favorite Sports.
	 */
	public List<Sport> getFavoriteSport(final long userid);

	/**
	 * Gets a User's favorite Club(s) based on Events joined.
	 * @param 	userid 	The User's id.
	 * @return the User's favorite Clubs.
	 */
	public List<Club> getFavoriteClub(final long userid);

	/**
	 * Deletes an Event from database along with all User related participations.
	 * @param	eventid		The Event's id.
	 */
	public void deleteEvent(final long eventid);

}
