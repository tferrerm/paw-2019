package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	private ClubDao cd;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";

	@Override
	public Optional<Club> findById(long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return cd.findById(clubid);
	}
	
	@Override
	public List<Club> findAll(int page) {
		if(page <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.findAll(page);
	}

	@Override
	public Club create(long userId, String name, String location) {
		return cd.create(userId, name, location);
	}
	
	@Override
	public void deleteClub(final long clubid) {
		cd.deleteClub(clubid);
	}
	
	@Override
	public Optional<Club> getPitchClub(final long pitchid) {
		return cd.getPitchClub(pitchid);
	}

}
