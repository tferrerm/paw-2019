package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Repository
public class PitchHibernateDao implements PitchDao {
	
	private static final int MAX_ROWS = 10;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Pitch> findById(long pitchid) {
		return Optional.of(em.find(Pitch.class, pitchid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		Query idQuery = em.createNativeQuery("SELECT pitchid FROM pitches WHERE clubid = :clubid ");
		idQuery.setParameter("clubid", clubid);
		idQuery.setFirstResult((page - 1) * MAX_ROWS);
		idQuery.setMaxResults(MAX_ROWS);
		final List<Long> ids = idQuery.getResultList();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Pitch> cq = cb.createQuery(Pitch.class);
		Root<Pitch> from = cq.from(Pitch.class);
		
		final TypedQuery<Pitch> query = em.createQuery(
				cq.select(from).where(from.get("pitchid").in(ids)).distinct(true)
			);
		
		return query.getResultList();
	}

	@Override
	public List<Pitch> findBy(Optional<String> name, Optional<String> sport, Optional<String> location,
			Optional<String> clubName, int page) {
		return Collections.emptyList();
	}

	@Override
	public Integer countFilteredPitches(Optional<String> pitchName, Optional<String> sport, Optional<String> location,
			Optional<String> clubName) {
		return 0;
	}

	@Override
	public int countPitchPages() {
		TypedQuery<Long> query = em.createQuery("SELECT count(*) FROM Pitch", Long.class);
		int rows = query.getSingleResult().intValue();
		int pageCount = rows / MAX_ROWS;
		if(rows % MAX_ROWS != 0)
			pageCount += 1;
		return pageCount;
	}

	@Override
	public Pitch create(Club club, String name, Sport sport) {
		final Pitch pitch = new Pitch(club, name, sport, Instant.now());
		em.persist(pitch);
		return pitch;
	}

	@Override
	public int getPageInitialPitchIndex(int pageNum) {
		return 1;
	}

	@Override
	public void deletePitch(long pitchid) {
		Pitch pitch = em.find(Pitch.class, pitchid);
		em.remove(pitch);
	}

}
