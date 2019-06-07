package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

	@Override
	public List<Pitch> findByClubId(long clubid, int page) {
		return Collections.emptyList();
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
		TypedQuery<Integer> query = em.createQuery("SELECT count(*) FROM Pitch", Integer.class);
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
