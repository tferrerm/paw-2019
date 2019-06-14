package ar.edu.itba.paw.interfaces;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

public interface TournamentDao {
	
	public Optional<Tournament> findById(long tournamentid);
	
	public List<Tournament> findBy(final int pageNum);
	
	public Optional<TournamentTeam> findByTeamId(final long teamid);

	public Tournament create(final String name, final Sport sport, final Club club, 
			final List<Pitch> availablePitches, final int maxTeams, final int teamSize, 
			final Instant firstRoundStartsAt, final Instant firstRoundEndsAt, 
			final Instant inscriptionEndsAt, final User user);

	public void joinTeam(Tournament tournament, TournamentTeam team, final User user) 
			throws UserBusyException, UserAlreadyJoinedException;
	
	public List<TournamentEvent> findTournamentEventsByTeam(final Tournament tournament, final TournamentTeam team);

}
