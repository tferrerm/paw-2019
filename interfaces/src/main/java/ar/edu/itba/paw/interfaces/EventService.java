package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventInPastException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InvalidDateFormatException;
import ar.edu.itba.paw.exception.InvalidVacancyNumberException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

public interface EventService {

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
	 * Gets Events for which a User has an inscription.
	 * @param futureEvents	Finds only future Events (true) or only past Events (false).
	 * @param userid		Inscripted User.
	 * @param pageNum		Page number.
	 * @return the list of events for which a User has an inscription.
	 */
	public List<Event> findByUserInscriptions(final boolean futureEvents, final long userid, final int pageNum);
	
	/**
	 * Counts the amount of Events for which a User has an inscription.
	 * @param futureEvents	Finds only future Events (true) or only past Events (false).
	 * @param userid		The User's id.
	 * @return the amount of events for which a User has an inscription.
	 */
	public Integer countByUserInscriptions(final boolean futureEvents, final long userid);

	public List<Event> findFutureEvents(int pageNum);

	public List<User> findEventUsers(final long eventid, final int pageNum);

	/**
	 * Finds Events in a Pitch. Only seven days of Events will be returned.
	 * @param pitchid 	The id of the Pitch.
	 * @return a list of Events (with a maximum size of 24 * 7).
	 */
	public List<Event> findCurrentEventsInPitch(final long pitchid);

	/**
	 * Returns a list of Events matching present filters and sets the inscriptions field.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param eventName			String to match an Event's name with.
	 * @param clubName			String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return  The list of Events matching present filters.
	 */
	public List<Event> findByWithInscriptions(boolean onlyFuture, Optional<String> eventName, 
			Optional<String> clubName, Optional<Sport> sport, Optional<String> organizer,
			Optional<String> vacancies, Optional<String> date, int pageNum)
			throws InvalidDateFormatException, InvalidVacancyNumberException;

	/**
	 * Returns a list of Events matching present filters.
	 * @param onlyFuture		Search only future Events (true) or any Events (false).
	 * @param eventName			String to match an Event's name with.
	 * @param clubName			String to match an Event's club name with.
	 * @param sport				String to match an Event's Sport with.
	 * @param organizer			String to match an Event's organizer's name with.
	 * @param vacancies			Minimum vacancies for an Event.
	 * @param pageNum			Page number.
	 * @return The list of Events matching present filters.
	 */
	public List<Event> findBy(boolean onlyFuture, Optional<String> eventName, Optional<String> clubName,
			Optional<Sport> sport, Optional<String> organizer, Optional<String> vacancies,
			Optional<String> date, int pageNum)
			throws InvalidDateFormatException, InvalidVacancyNumberException;
	
	public Integer countFilteredEvents(final boolean onlyFuture, final Optional<String> eventName, 
			final Optional<String> clubName, final Optional<Sport> sport, Optional<String> organizer, 
			final Optional<String> vacancies, final Optional<String> date)
			throws InvalidDateFormatException, InvalidVacancyNumberException;

	public boolean[][] convertEventListToSchedule(List<Event> events, int minHour,
			int maxHour, int dayAmount);
	
	public Event[][] convertEventListToSchedule(List<Event> events, int dayAmount, int maxAmountOfEvents);

	public int countUserEventPages(final long userid);

	public int countFutureEventPages();

	/**
	 * Gets the amount of Users currently participating in an Event.
	 * @param	eventid		The Event's id
	 * @return the amount of Users currently participating in the Event.
	 */
	public int countParticipants(long eventid);

	public Event create(final String name, final User owner, final Pitch pitch, final String description,
			final String maxParticipants, final String date, final String startsAtHour,
			final String endsAtHour) throws InvalidDateFormatException, EventInPastException, 
											MaximumDateExceededException, EndsBeforeStartsException, 
											EventOverlapException, HourOutOfRangeException;

	public void joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, EventFullException, UserBusyException;

	public void leaveEvent(final User user, final Event event);

	public void kickFromEvent(final User owner, final long kickedUserId, final Event event)
			throws UserNotAuthorizedException;

	public String[] getScheduleDaysHeader();

	public Map<Integer, String> getAvailableHoursMap(int minHour, int maxHour);

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
	public void deleteEvent(long eventid);
	
	/**
	 * Gets the sum of User votes for an Event.
	 * @param eventid	The Event's id.
	 * @return the sum of all User votes for that Event.
	 */
	public int getVoteBalance(final long eventid);
	
	/**
	 * Gets the User's vote for that event.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 * @return -1 if downvote, 1 if upvote or 0 if such vote does not exist.
	 */
	public int getUserVote(final long eventid, final long userid);
	
	/**
	 * Sets a User's vote for an Event.
	 * @param isUpvote	True for upvote, false for downvote.
	 * @param eventid	The Event's id.
	 * @param userid	The User's id.
	 */
	public void vote(final boolean isUpvote, final Event event, final long userid)
			throws UserNotAuthorizedException, EventNotFinishedException;
	
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
