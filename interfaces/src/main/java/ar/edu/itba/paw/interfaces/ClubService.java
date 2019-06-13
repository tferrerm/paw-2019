package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;

public interface ClubService {
	
	public Optional<Club> findById(long clubid);
	
	public List<Club> findAll(int page);
	
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page);
	
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location);
	
	public Club create(String name, String location);
	
	/**
	 * Gets the page's first Club's index in the overall filtered Clubs.
	 * @param pageNum	The page's number.
	 * @return the page's first Club's index.
	 */
	public int getPageInitialClubIndex(final int pageNum);
	
	public void deleteClub(long clubid);
	
	public int countClubPages();
	
	/**
	 * Gets the amount of Events which took place in a Club.
	 * @param clubid	The id of the Club.
	 * @return the amount of past Events for that Club.
	 */
	public int countPastEvents(final long clubid);
	
	public ClubComment createComment(final long userid, final long clubid, final String comment)
			throws UserNotAuthorizedException;

	/**
	 * Returns true if the commenter has an Inscription for a past Event which took place in the Club
	 * or if the User has owned a past Event which took place in the Club.
	 * @param commenterid	The commenter's id.
	 * @param clubid		The Club's id.
	 * @return true if the commenter has an Inscription for a past Event which took place in the Club
	 * or if the User has owned a past Event which took place in the Club.
	 */
	public boolean haveRelationship(final long commenterid, final long clubid);
	
	public List<ClubComment> getCommentsByClub(final long clubid, final int pageNum);
	
	public int countByClubComments(final long clubid);
	
	public int getCommentsPageInitIndex(final int pageNum);

	public int getCommentsMaxPage(final long clubid);

}
