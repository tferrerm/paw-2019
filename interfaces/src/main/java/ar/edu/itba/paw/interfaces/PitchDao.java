package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

public interface PitchDao {
	
	public Optional<Pitch> findById(long pitchid);
	
	public List<Pitch> findByClubId(long clubid, int page);
	
	public List<Pitch> findBy(Optional<String> name, Optional<String> sport,
			Optional<String> location, Optional<String> clubName, int page);
	
	public Pitch create(Club club, String name, Sport sport);
	
	public void deletePitch(long pitchid);

}