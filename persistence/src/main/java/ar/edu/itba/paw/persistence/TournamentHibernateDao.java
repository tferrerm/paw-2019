package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.User;

@Repository
public class TournamentHibernateDao implements TournamentDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;
	
	public Optional<Tournament> findById(long tournamentid) {
		return Optional.empty();
	}

	@Override
	public Tournament create(final String name, final Sport sport, final Club club, 
			final List<Pitch> availablePitches, final int maxTeams, final int teamSize, 
			final Instant firstRoundStartsAt, final Instant firstRoundEndsAt, 
			final Instant inscriptionEndsAt, final User user) {
		
		final Tournament tournament = new Tournament(name, sport, club, maxTeams, teamSize,
				inscriptionEndsAt);
		Instant startsAt = firstRoundStartsAt;
		Instant endsAt = firstRoundEndsAt;
		// CREAR TOURNAMENT EVENTS
		for(int i = 0; i < tournament.getRounds(); i++) {
			for(int j = 0; j < maxTeams / 2; j++) {
				StringBuilder eventName = new StringBuilder(name).append(" - R").append(i+1); // INTERNACIONALIZACION?
				
				Event event = new Event(eventName.toString(), user, availablePitches.get(i), 
						teamSize * 2, startsAt, endsAt);
				em.persist(event);
				TournamentEvent tournamentEvent = new TournamentEvent(tournament, event, i+1);
				em.persist(tournamentEvent);
			}
			
			startsAt = startsAt.plus(1, ChronoUnit.WEEKS); // FUNCIONA?
			endsAt = endsAt.plus(1, ChronoUnit.WEEKS);
		}
		em.persist(tournament);
		return tournament;
	}

}
