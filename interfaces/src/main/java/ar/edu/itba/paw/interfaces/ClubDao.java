package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;

public interface ClubDao {
	
	public Optional<Club> findById(long clubid);
	
	public List<Club> findAll(int page);
	
	public Club create(long userId, String name, String location);
	
	public void deleteClub(long clubid);
	
	/**
	 * Gets the Club in which a Pitch is located if any.
	 * @param pitchid	The id of the Pitch.
	 * @return the Club in which the Pitch is located if any.
	 */
	public Optional<Club> getPitchClub(final long pitchid);

}
