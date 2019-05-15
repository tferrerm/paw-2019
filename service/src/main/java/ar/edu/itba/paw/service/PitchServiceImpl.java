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
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page must be greater than zero.";
	
	@Override
	public Optional<Pitch> findById(long pitchid) {
		if(pitchid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return pd.findById(pitchid);
	}

	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		if(clubid <= 0 || page <= 0) {
			throw new IllegalArgumentException("Parameters must be greater than zero.");
		}
		return pd.findByClubId(clubid, page);
	}
	
	@Override
	public List<Pitch> findBy(Optional<String> name, Optional<Sport> sport,
			Optional<String> location, Optional<String> clubName, int page) {
		if(page <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		return pd.findBy(name, Optional.ofNullable(sportString), location, clubName, page);
	}

	@Override
	public Pitch create(Club club, String name, Sport sport) {
		return pd.create(club, name, sport);
	}
	
	@Override
	public void deletePitch(long pitchid) {
		pd.deletePitch(pitchid);
	}

}