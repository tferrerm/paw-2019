package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;

public interface ClubDao {
	
	public Optional<Club> findById(long clubid);
	
	public List<Club> findAll(int page);
	
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page);
	
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location);
	
	public Club create(long userId, String name, String location);
	
	/**
	 * Gets the page's first Club's index in the overall filtered Clubs.
	 * @param pageNum	The page's number.
	 * @return the page's first Club's index.
	 */
	public int getPageInitialClubIndex(final int pageNum);
	
	public void deleteClub(long clubid);
	
	/**
	 * Gets the Club in which a Pitch is located if any.
	 * @param pitchid	The id of the Pitch.
	 * @return the Club in which the Pitch is located if any.
	 */
	public Optional<Club> getPitchClub(final long pitchid);

	public int countClubPages();
	
	/**
	 * Gets the amount of Events which took place in a Club.
	 * @param clubid	The id of the Club.
	 * @return the amount of past Events for that Club.
	 */
	public int countPastEvents(final long clubid);

}
