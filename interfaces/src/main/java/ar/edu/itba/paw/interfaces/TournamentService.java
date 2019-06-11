package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.Tournament;

public interface TournamentService {
	
	public Optional<Tournament> findById(long tournamentid);
	
}
