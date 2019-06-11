package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Tournament;

//@Service
public class TournamentServiceImpl implements TournamentService {
	
	//@Autowired
	private TournamentDao td;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	
	@Override
	public Optional<Tournament> findById(long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return td.findById(tournamentid);
	}
	
}
