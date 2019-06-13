package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;

public interface TournamentService {
	
	public Optional<Tournament> findById(long tournamentid);
	
	public Tournament create(final String name, final Sport sport, final Club club, final String maxTeams,
			final String teamSize, final String firstRoundDate, final String startsAtHour,
			final String endsAtHour, final String inscriptionEndDate);
	
}
