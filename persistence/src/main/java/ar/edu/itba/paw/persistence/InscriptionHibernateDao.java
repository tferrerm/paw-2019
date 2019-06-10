package ar.edu.itba.paw.persistence;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.InscriptionDao;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.InscriptionId;

@Repository
public class InscriptionHibernateDao implements InscriptionDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Optional<Inscription> findByIds(final long eventid, final long userid) {
		return Optional.of(em.find(Inscription.class, new InscriptionId(eventid, userid)));
	}
	
	@Override
	public void deleteInscription(final long eventid, final long userid) {
		Inscription inscription = findByIds(eventid, userid)
				.orElseThrow(NoSuchElementException::new);
		em.remove(inscription);
	}
	
}
