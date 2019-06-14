package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;

@Repository
public class TournamentHibernateDao implements TournamentDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;
	
	public Optional<Tournament> findById(long tournamentid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tournament> cq = cb.createQuery(Tournament.class);
		Root<Tournament> from = cq.from(Tournament.class);
		from.fetch("teams", JoinType.LEFT);
		//from.fetch("events", JoinType.LEFT);
		
		final TypedQuery<Tournament> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("tournamentid"), tournamentid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Tournament create(final String name, final Sport sport, final Club club, 
			final List<Pitch> availablePitches, final int maxTeams, final int teamSize, 
			final Instant firstRoundStartsAt, final Instant firstRoundEndsAt, 
			final Instant inscriptionEndsAt, final User user) {
		
		final Tournament tournament = new Tournament(name, sport, club, maxTeams, teamSize,
				inscriptionEndsAt);
		em.persist(tournament);
		
		/* Creation of teams */
		List<TournamentTeam> teams = new ArrayList<>();
		for(int i = 1; i <= maxTeams; i++) {
			StringBuilder teamName = new StringBuilder("Team ").append(i);
			TournamentTeam team = new TournamentTeam(teamName.toString(), tournament);
			teams.add(team);
			em.persist(team);
		}
		
		/* Creation of tournament events by round */
		Instant startsAt = firstRoundStartsAt;
		Instant endsAt = firstRoundEndsAt;
		for(int i = 0; i < tournament.getRounds(); i++) {
			StringBuilder eventName = new StringBuilder(name).append(" - R").append(i+1); // INTERNACIONALIZACION?
			for(int j = 0; j < maxTeams/2; j++) {
				Event event = new Event(eventName.toString(), user, availablePitches.get(j), 
						teamSize * 2, startsAt, endsAt); // PASAR A EVENT DAO
				em.persist(event);
				TournamentEvent tournamentEvent = new TournamentEvent(tournament, event, i + 1,
						teams.get(j), teams.get(j + maxTeams/2));
				em.persist(tournamentEvent);
			}
			startsAt = startsAt.plus(7, ChronoUnit.DAYS);
			endsAt = endsAt.plus(7, ChronoUnit.DAYS);
			teamsRoundRobin(teams);
		}
		
		return tournament;
	}
	
	private void teamsRoundRobin(List<TournamentTeam> teams) {
		TournamentTeam aux = teams.get(1);
		int size = teams.size();
		for(int i = 2; i < size/2; i++) {
			aux = teams.set(i, aux);
		}
		aux = teams.set(size - 1, aux);
		for(int i = size - 2; i >= size/2; i--) {
			aux = teams.set(i, aux);
		}
		teams.set(1, aux);
	}

}
