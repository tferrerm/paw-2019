package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
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
		from.fetch("tournamentEvents", JoinType.LEFT);
		
		final TypedQuery<Tournament> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("tournamentid"), tournamentid))
			);
		
		return query.getResultList().stream().findFirst();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tournament> findBy(int pageNum) {
		
		String idQueryString = "SELECT tournamentid FROM tournaments NATURAL JOIN clubs ORDER BY clubname ASC";
		Query idQuery = em.createNativeQuery(idQueryString);
		idQuery.setFirstResult((pageNum - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		List<Long> ids =  idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tournament> cq = cb.createQuery(Tournament.class);
		Root<Tournament> from = cq.from(Tournament.class);
		from.fetch("tournamentClub");
		final TypedQuery<Tournament> query = em.createQuery(
				cq.select(from).where(from.get("tournamentid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}
	
	@Override
	public Optional<TournamentTeam> findByTeamId(final long teamid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TournamentTeam> cq = cb.createQuery(TournamentTeam.class);
		Root<TournamentTeam> from = cq.from(TournamentTeam.class);
		from.fetch("inscriptions", JoinType.LEFT);
		
		final TypedQuery<TournamentTeam> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("teamid"), teamid))
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

	@Override
	public void joinTeam(Tournament tournament, TournamentTeam team, final User user) 
			throws UserBusyException, UserAlreadyJoinedException {
		
		/* Only checks for current week as tournaments must start within it and dates don´t change */
		String userBusyQueryString = "SELECT count(i) FROM Inscription AS i WHERE "
				+ " i.inscriptedUser.userid = :userid AND "
				+ " ((i.inscriptionEvent.startsAt <= :startsAt AND i.inscriptionEvent.endsAt > :startsAt) OR "
				+ " (i.inscriptionEvent.startsAt > :startsAt AND i.inscriptionEvent.startsAt < :endsAt))";
		
		TypedQuery<Long> query = em.createQuery(userBusyQueryString.toString(), Long.class);
		query.setParameter("userid", user.getUserid());
		Instant firstRoundStartsAt = null;
		Instant secondRoundStartsAt = null;
		for(TournamentEvent te : tournament.getTournamentEvents()) {
			if(te.getRound() == 1) {
				firstRoundStartsAt = te.getEvent().getStartsAt();
				secondRoundStartsAt = te.getEvent().getEndsAt();
				break;
			}
		}
		query.setParameter("startsAt", firstRoundStartsAt);
		query.setParameter("endsAt", secondRoundStartsAt);
		
		int userBusyQueryResult = query.getSingleResult().intValue();
		
		if(userBusyQueryResult > 0)
			throw new UserBusyException("User " + user.getUserid() + " already joined "
					+ "an event in that period");
		
		/* Creates an inscription for every tournament round */
		List<TournamentEvent> tournamentEvents = findTournamentEventsByTeam(tournament, team);
		for(TournamentEvent tournamentEvent : tournamentEvents) {
			try {
				em.persist(new Inscription(tournamentEvent.getEvent(), user, team));
			} catch(EntityExistsException e) {
				throw new UserAlreadyJoinedException("User " + user.getUserid() + " already joined event "
						+ tournamentEvent.getEventid());
			}
		}
		
	}

	@Override
	public List<TournamentEvent> findTournamentEventsByTeam(final Tournament tournament, final TournamentTeam team) {
		String queryString = "FROM TournamentEvent AS te "
				+ " WHERE te.tournament.tournamentid = :tournamentid"
				+ " AND (te.firstTeam.teamid = :teamid OR te.secondTeam.teamid = :teamid)";
		
		TypedQuery<TournamentEvent> query = em.createQuery(queryString, TournamentEvent.class);
		query.setParameter("tournamentid", tournament.getTournamentid());
		query.setParameter("teamid", team.getTeamId());
		/* For caution */
		query.setMaxResults(tournament.getRounds());
		
		return query.getResultList();
	}

}
