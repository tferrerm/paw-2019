package ar.edu.itba.paw.persistence;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
	public Optional<Integer> getVoteBalance(long eventid) {
		StringBuilder queryString = new StringBuilder("SELECT sum(i.vote) FROM Inscription AS i "
				+ " WHERE i.inscriptionEvent.eventid = :eventid ");
		
		TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
		query.setParameter("eventid", eventid);
		
		List<Long> balance = query.getResultList();
		if(balance.isEmpty() || balance.get(0) == null)
			return Optional.empty();
		return Optional.of(balance.get(0).intValue());
	}

	@Override
	public Optional<Integer> getUserVote(long eventid, long userid) {
		StringBuilder queryString = new StringBuilder("SELECT i.vote FROM Inscription AS i "
				+ " WHERE i.inscriptionEvent.eventid = :eventid AND i.inscriptedUser.userid = :userid");
		
		TypedQuery<Integer> query = em.createQuery(queryString.toString(), Integer.class);
		query.setParameter("eventid", eventid);
		query.setParameter("userid", userid);
		
		List<Integer> vote = query.getResultList();
		if(vote.isEmpty() || vote.get(0) == null)
			return Optional.empty();
		return Optional.of(vote.get(0).intValue());
	}

	@Override
	public int vote(boolean isUpvote, long eventid, long userid) {
		Query query = em.createQuery("UPDATE Inscription AS i SET i.vote = :vote "
				+ " WHERE i.inscriptionEvent.eventid = :eventid "
				+ " AND i.inscriptedUser.userid = :userid");
		query.setParameter("vote", (isUpvote)? 1 : -1);
		query.setParameter("eventid", eventid);
		query.setParameter("userid", userid);
		return query.executeUpdate();
	}
	
	@Override
	public void deleteInscription(final long eventid, final long userid) {
		Inscription inscription = findByIds(eventid, userid)
				.orElseThrow(NoSuchElementException::new);
		em.remove(inscription);
	}
	
}
