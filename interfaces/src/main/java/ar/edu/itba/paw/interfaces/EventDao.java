package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.EventOverlapException;
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
	 * @param userid 		Owner of Events.
	 * @param pageNum 		Page number.
	 * @return The list of owned Events.
	 */
	public List<Event> findByOwner(final boolean futureEvents, final long userid, final int pageNum);
	
	/**
	 * Counts Events owned by a User.
	 * @param futureEvents 	Finds only future Events (true) or only past Events (false).
	 * @param userid 		Owner of Events.
	 * @return The amount of owned Events.
	 */
	public int countByOwner(final boolean futureEvents, final long userid);
	
	/**
	 * Gets future Events for which a User has an inscription. Maximum Events: 24 * 7
	 * @param userid 		Inscripted User.
	 * @param pageNum 		Page number.
	 * @return a list of Events.
	 */
	public List<Event> findFutureUserInscriptions(long userid);
	
	/**
	 * Gets Events in which the User participated.
	 * @param userid		Inscripted User.
	 * @param pageNum		Page number.
	 * @return a list of Events
	 */
	public List<Event> findPastUserInscriptions(long userid, int pageNum);
	
	/**
	 * Counts Events for which a User has an inscription.
	 * @param futureEvents	Finds only future Events (true) or only past Events (false).
	 * @param userid		The User's id.
	 * @return the amount of events for which a User has an inscription.
	 */
	public Integer countByUserInscriptions(final boolean futureEvents, final long userid);

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
	 * @param eventName			String to match an Event's name with.
	 * @param establishment		String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return a list of Events that matched given filters.
	 */
	public List<Event> findBy(boolean onlyFuture, Optional<String> eventName, 
			Optional<String> establishment, Optional<String> sport, 
			Optional<String> organizer, Optional<Integer> vacancies, int pageNum);
	
	/**
	 * Returns the amount of Events matching present filters.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param name				String to match an Event's name with.
	 * @param establishment		String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @return the amount of Events that matched given filters.
	 */
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<String> sport, 
			final Optional<String> organizer, final Optional<Integer> vacancies);

	/**
	 * Returns a combination of eventid and vacancies for that Event.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param eventName			String to match an Event's name with.
	 * @param establishment		String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return
	 */
	public List<Long[]> countBy(boolean onlyFuture, Optional<String> eventName, 
			Optional<String> establishment, Optional<String> sport, 
			Optional<String> organizer, Optional<Integer> vacancies, int pageNum);

	public int countUserEventPages(final long userid);

	public int countFutureEventPages();

	/**
	 * Gets the amount of Users currently participating in an Event.
	 * @param	eventid		The Event's id
	 * @return the amount of Users currently participating in the Event.
	 */
	public int countParticipants(final long eventid);

	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final int maxParticipants, final Instant startsAt, final Instant endsAt)
					throws EventOverlapException;

	public void joinEvent(final User user, final Event event)
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
	public Optional<Sport> getFavoriteSport(final long userid);

	/**
	 * Gets a User's favorite Club(s) based on Events joined.
	 * @param 	userid 	The User's id.
	 * @return the User's favorite Clubs.
	 */
	public Optional<Club> getFavoriteClub(final long userid);
	
	/**
	 * Gets the page's first Event's index in the overall filtered Events.
	 * @param pageNum	The page's number.
	 * @return the page's first Event's index.
	 */
	public int getPageInitialEventIndex(final int pageNum);

	/**
	 * Deletes an Event from database along with all User related participations.
	 * @param	eventid		The Event's id.
	 */
	public void deleteEvent(final long eventid);
	
	/**
	 * Gets the sum of User votes for an Event, or empty Optional if no votes.
	 * @param eventid	The Event's id.
	 * @return the sum of all User votes for that Event.
	 */
	public Optional<Integer> getVoteBalance(final long eventid);
	
	/**
	 * Gets the User's vote for that event.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return -1 if downvote, 1 if upvote or empty Optional if such vote does not exist.
	 */
	public Optional<Integer> getUserVote(final long eventid, final long userid);
	
	/**
	 * Sets a User's vote for an Event.
	 * @param isUpvote	True for upvote, false for downvote.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return 0 if the User doesn't have an inscription for that Event, 1 otherwise.
	 */
	public int vote(final boolean isUpvote, final long eventid, final long userid);
	
	/**
	 * Gets the amount of pages the list of inscripted Events occupies.
	 * @param onlyFuture	Search only for future Events (true) or only for past Events (false)
	 * @param userid		The User's id.
	 * @return The amount of pages.
	 */
	public int countUserInscriptionPages(final boolean onlyFuture, final long userid);
	
	/**
	 * Gets the amount of pages the list of owned Events occupies.
	 * @param onlyFuture	Search only for future Events (true) or only for past Events (false)
	 * @param userid		The User's id.
	 * @return The amount of pages.
	 */
	public int countUserOwnedPages(final boolean onlyFuture, final long userid);

}
