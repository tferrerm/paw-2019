package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;

public interface ClubService {
	
	public Optional<Club> findById(long clubid);
	
	public List<Club> findAll(int page);
	
	public Club create(long userId, String name, String location);

}
