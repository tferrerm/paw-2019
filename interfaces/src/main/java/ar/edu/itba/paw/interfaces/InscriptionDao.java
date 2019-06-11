package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.Inscription;

public interface InscriptionDao {
	
	public Optional<Inscription> findByIds(final long eventid, final long userid);
	
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
	
	public void deleteInscription(final long eventid, final long userid);
	
}
