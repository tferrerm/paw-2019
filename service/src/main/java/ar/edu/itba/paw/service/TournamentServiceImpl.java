package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

@Service
public class TournamentServiceImpl implements TournamentService {
	
	@Autowired
	private TournamentDao td;
	
	@Autowired
	private ClubDao cd;
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";
	
	@Override
	public Optional<Tournament> findById(long tournamentid) {
		if(tournamentid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return td.findById(tournamentid);
	}
	
	@Override
	public List<Tournament> findBy(int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}

		return td.findBy(pageNum);
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

}
