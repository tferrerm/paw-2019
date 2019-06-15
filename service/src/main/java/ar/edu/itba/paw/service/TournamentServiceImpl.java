package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

@Service
public class TournamentServiceImpl implements TournamentService {
	
	@Autowired
	private TournamentDao td;
	
	@Autowired
	private ClubDao cd;
	
	@Autowired
	private UserDao ud;
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";
	
	@Override
	public Optional<Tournament> findById(final long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return td.findById(tournamentid);
	}
	
	@Override
	public List<Tournament> findBy(final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}

		return td.findBy(pageNum);
	}
	
	@Override
	public Optional<TournamentTeam> findByTeamId(final long teamid) {
		if(teamid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}

		return td.findByTeamId(teamid);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public Tournament create(final String name, final Sport sport, final Club club, final String maxTeams,
			final String teamSize, final String firstRoundDate, final String startsAtHour,
			final String endsAtHour, final String inscriptionEndDate, final User user) {
		
		int mt = Integer.parseInt(maxTeams);
		int ts = Integer.parseInt(teamSize);
		int startsAt = Integer.parseInt(startsAtHour);
    	int endsAt = Integer.parseInt(endsAtHour);
    	Instant firstRoundInstant = LocalDate.parse(firstRoundDate)
    			.atStartOfDay(ZoneId.of(TIME_ZONE)).toInstant();
    	Instant inscriptionEndInstant = LocalDateTime.parse(inscriptionEndDate)
    			.atZone(ZoneId.of(TIME_ZONE)).toInstant();
    	Instant firstRoundStartsAt = firstRoundInstant.plus(startsAt, ChronoUnit.HOURS);
    	Instant firstRoundEndsAt = firstRoundInstant.plus(endsAt, ChronoUnit.HOURS);
    	
    	// CHEQUEOS
    	
    	// VALIDAR QUE HAYA SUFICIENTES CANCHAS (IF SIZE < MT/2...)
    	List<Pitch> availablePitches = cd.getAvailablePitches(club.getClubid(), sport, 
    			firstRoundStartsAt, firstRoundEndsAt, mt/2);
    	
    	return td.create(name, sport, club, availablePitches, mt, ts, firstRoundStartsAt,
    			firstRoundEndsAt, inscriptionEndInstant, user);
    	
    	
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void joinTournament(long tournamentid, long teamid, final long userid) 
			throws UserBusyException, UserAlreadyJoinedException {
		if(tournamentid <= 0 || teamid <= 0 || userid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		} // IF NO ARRANCO, IF TEAM NO LLENO
		Tournament tournament = td.findById(tournamentid).orElseThrow(NoSuchElementException::new);
		TournamentTeam team = td.findByTeamId(teamid).orElseThrow(NoSuchElementException::new);
		final User user = ud.findById(userid).orElseThrow(NoSuchElementException::new);
		
		td.joinTournament(tournament, team, user);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void leaveTournament(final long tournamentid, final long userid) {
		// IF NO ARRANCO
		Tournament tournament = td.findById(tournamentid).orElseThrow(NoSuchElementException::new);
		User user = ud.findById(userid).orElseThrow(NoSuchElementException::new);
		TournamentTeam team = td.findUserTeam(tournament, user).orElseThrow(NoSuchElementException::new);
		td.deleteTournamentInscriptions(team, user);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void kickFromTournament(final User kickedUser, final Tournament tournament) {
		TournamentTeam team = td.findUserTeam(tournament, kickedUser).orElseThrow(NoSuchElementException::new);
		td.deleteTournamentInscriptions(team, kickedUser);
	}
	
	@Override
	public Optional<TournamentTeam> findUserTeam(final long tournamentid, final long userid) {
		if(tournamentid <= 0 || userid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		Tournament tournament = td.findById(tournamentid).orElseThrow(NoSuchElementException::new);
		final User user = ud.findById(userid).orElseThrow(NoSuchElementException::new);
		return td.findUserTeam(tournament, user);
	}

	@Override
	public List<TournamentTeam> findTournamentTeams(final long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return td.findTournamentTeams(tournamentid);
	}

	@Override
	public Map<Long, List<User>> getTeamsUsers(long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		Tournament tournament = td.findById(tournamentid).orElseThrow(NoSuchElementException::new);
		Map<Long, List<User>> teamsUsersMap = new HashMap<>();
		for(TournamentTeam team : tournament.getTeams()) {
			List<User> teamMembers = td.findTeamMembers(team);
			teamsUsersMap.put(team.getTeamid(), teamMembers);
		}
		return teamsUsersMap;
	}

	@Override
	public boolean inscriptionEnded(Tournament tournament) {
		return tournament.getEndsInscriptionAt().compareTo(Instant.now()) < 0;
	}

	@Override
	public Map<TournamentTeam, Integer> getTeamsScores(Tournament tournament) {
		Map<TournamentTeam, Integer> teamsScoresMap = new HashMap<>();
		for(TournamentTeam team : tournament.getTeams()) {
			teamsScoresMap.put(team, team.getTeamScore());
		}
		return teamsScoresMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(
	            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                    LinkedHashMap::new));
	}

}
