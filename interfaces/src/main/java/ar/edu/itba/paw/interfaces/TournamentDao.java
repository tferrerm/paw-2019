package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

public interface TournamentDao {
	
	public Optional<Tournament> findById(long tournamentid);
	
	public List<Tournament> findBy(final int pageNum);

	public Tournament create(final String name, final Sport sport, final Club club, 
			final List<Pitch> availablePitches, final int maxTeams, final int teamSize, 
			final Instant firstRoundStartsAt, final Instant firstRoundEndsAt, 
			final Instant inscriptionEndsAt, final User user);

}
