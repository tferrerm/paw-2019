package ar.edu.itba.paw.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

public interface TournamentService {
	
	public Optional<Tournament> findById(long tournamentid);
	
	public List<Tournament> findBy(final int pageNum);
	
	public Optional<TournamentTeam> findByTeamId(final long teamid);
	
	public Tournament create(final String name, final Sport sport, final Club club, final String maxTeams,
			final String teamSize, final String firstRoundDate, final String startsAtHour,
			final String endsAtHour, final String inscriptionEndDate, final User user);

	public void joinTournament(final long tournamentid, final long teamid, final long userid) 
			throws UserBusyException, UserAlreadyJoinedException;
	
	public void leaveTournament(final long tournamentid, final long userid);
	
	public void kickFromTournament(final User kickedUser, final Tournament tournament);
	
	public Optional<TournamentTeam> findUserTeam(final long tournamentid, final long userid);

	public List<TournamentTeam> findTournamentTeams(final long tournamentid);

	public Map<Long, List<User>> getTeamsUsers(final long tournamentid);

	public boolean inscriptionEnded(final Tournament tournament);

	public Map<TournamentTeam, Integer> getTeamsScores(final Tournament tournament);

	public List<TournamentEvent> findTournamentEventsByRound(final long tournamentid, final int roundPage);

	public void postTournamentEventResult(final Tournament tournament, final long eventid, final Integer firstResult, final Integer secondResult);
	
}
