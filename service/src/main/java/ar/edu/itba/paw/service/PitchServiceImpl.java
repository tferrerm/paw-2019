package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Service
public class PitchServiceImpl implements PitchService {
	
	@Autowired
	private PitchDao pd;
	
	@Override
	public Optional<Pitch> findById(long pitchid) {
		return pd.findById(pitchid);
	}

	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		return pd.findByClubId(clubid, page);
	}
	
	@Override
	public List<Pitch> findBy(Optional<String> name, Optional<Sport> sport,
			Optional<String> location, int page) {
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		return pd.findBy(name, Optional.ofNullable(sportString), location, page);
	}

	@Override
	public Pitch create(Club club, String name, Sport sport) {
		return pd.create(club, name, sport);
	}

}
