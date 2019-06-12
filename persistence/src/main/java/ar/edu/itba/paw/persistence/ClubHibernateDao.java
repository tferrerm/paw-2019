package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.User;

@Repository
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
		Query idQuery = em.createNativeQuery("SELECT clubid FROM clubs ");
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		
		final TypedQuery<Club> query = em.createQuery(
				cq.select(from).where(from.get("clubid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int page) {
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT clubid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, clubName, location));
		idQueryString.append(" ORDER BY clubname ASC ");
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		if(ids.isEmpty())
			return Collections.emptyList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Club> cq = cb.createQuery(Club.class);
		Root<Club> from = cq.from(Club.class);
		
		final TypedQuery<Club> query = em.createQuery(
				cq.select(from).where(from.get("clubid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location) {
		Map<String, Object> paramsMap = new HashMap<>();
		StringBuilder idQueryString = new StringBuilder("SELECT clubid ");
		idQueryString.append(getFilterQueryEndString(paramsMap, clubName, location));
		
		Query idQuery = em.createNativeQuery(idQueryString.toString());
		for(Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			idQuery.setParameter(entry.getKey(), entry.getValue());
		}
		
		return idQuery.getResultList().size();
	}
	
	private String getFilterQueryEndString(Map<String, Object> paramsMap, 
			final Optional<String> clubName, final Optional<String> location) {
		Filter[] params = { 
				new Filter("clubname", clubName),
				new Filter("location", location)
		};
		StringBuilder queryString = new StringBuilder(" FROM clubs ");
		for(Filter param : params) {
			if(param.getValue().isPresent() && !isEmpty(param.getValue())) {
				int paramNum = paramsMap.size();
				queryString.append(buildPrefix(paramNum));
				queryString.append(param.queryAsString(paramNum));
				paramsMap.put(Filter.getParamName() + paramNum, param.getValue().get());
			}
		}
		return queryString.toString();
	}
	
	private boolean isEmpty(Optional<?> opt) {
		return opt.get().toString().isEmpty();
	}
	
	private String buildPrefix(int currentFilter) {
		if(currentFilter == 0)
			return " WHERE ";
		return " AND ";
	}

	@Override
	public Club create(String name, String location) {
		final Club club = new Club(name, location, Instant.now());
		em.persist(club);
		return club;
	}

	@Override
	public int getPageInitialClubIndex(int pageNum) {
		return (pageNum -1) * MAX_ROWS + 1;
	}

	@Override
	public void deleteClub(long clubid) {
		Club club = em.find(Club.class, clubid);
		em.remove(club);
	}

	@Override
	public int countClubPages() {
		TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Club", Long.class);
		int rows = query.getSingleResult().intValue();
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public int countPastEvents(long clubid) {
		TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Event AS e "
				+ " WHERE e.endsAt < :now AND e.pitch.club.clubid = :clubid", Long.class);
		query.setParameter("now", Instant.now());
		query.setParameter("clubid", clubid);
		return query.getSingleResult().intValue();
	}
	
	@Override
	public ClubComment createComment(final User commenter, final Club club, final String comment) {
		
		final ClubComment clubComment = new ClubComment(commenter, club, comment);
		em.persist(clubComment);
		
		return clubComment;
	}

}
