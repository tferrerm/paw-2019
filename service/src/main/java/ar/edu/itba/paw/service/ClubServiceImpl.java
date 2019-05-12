package ar.edu.itba.paw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.User;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	private ClubDao cd;

	@Override
	public Optional<Club> findById(long clubid) {
		return cd.findById(clubid);
	}

	@Override
	public List<Club> findByOwnerId(long ownerid) {
		return cd.findByOwnerId(ownerid);
	}
	
	@Override
	public List<Club> findAll(int page) {
		return cd.findAll(page);
	}

	@Override
	public Club create(User owner, String name, String location) {
		return cd.create(owner, name, location);
	}

}
