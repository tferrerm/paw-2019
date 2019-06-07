package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.model.Club;

public class ClubHibernateDao implements ClubDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Club> findById(long clubid) {
		return Optional.of(em.find(Club.class, clubid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Club> findAll(int page) {
		Query idQuery = em.createNativeQuery("SELECT clubid FROM clubs;");
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = (List<Long>) idQuery.getResultList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		
		final TypedQuery<Club> query = em.createQuery(cq.select(from).where(
				cb.isMember(ids, from.get("clubid"))).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page) {
		
	}

	@Override
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location) {
		
	}

	@Override
	public Club create(String name, String location) {
		final Club club = new Club(name, location, Instant.now());
		em.persist(club);
		return club;
	}

	@Override
	public int getPageInitialClubIndex(int pageNum) {
		
	}

	@Override
	public void deleteClub(long clubid) {
		Club club = em.find(Club.class, clubid);
		em.remove(club);
	}

	@Override
	public int countClubPages() {
		TypedQuery<Integer> query = em.createQuery("SELECT count(*) FROM Club", Integer.class);
		int rows = query.getSingleResult().intValue();
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public int countPastEvents(long clubid) {
		TypedQuery<Integer> query = em.createQuery("SELECT count(*) FROM Event AS e WHERE e.endsAt < :now AND e.pitch.club.clubid = :clubid", Integer.class);
		query.setParameter("now", Instant.now());
		query.setParameter("clubid", clubid);
		return query.getSingleResult().intValue();
	}

}
