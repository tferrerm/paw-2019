package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

public interface PitchService {
	
	public Optional<Pitch> findById(long pitchid);
	
	public List<Pitch> findByClubId(long clubid);
	
	public List<Pitch> findBy(Optional<String> name, Optional<Sport> sport,
			Optional<String> location);
	
	public Pitch create(Club club, String name, Sport sport);

}
