package ar.edu.itba.paw.persistence;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

@Repository
public class UserHibernateDao implements UserDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<User> findById(long userid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		from.fetch("comments", JoinType.LEFT);
		
		final TypedQuery<User> query = em.createQuery(
				cq.select(from).where(cb.equal(from.get("userid"), userid))
			);
		
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		final TypedQuery<User> query = em.createQuery("FROM User AS u WHERE u.username LIKE :username",
				User.class);
		query.setParameter("username", username);
		query.setMaxResults(1);
		return query.getResultList().stream().findFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Integer> countVotesReceived(long userid) {
		Query votesQuery = em.createNativeQuery("SELECT sum(vote) FROM events_users "
				+ " WHERE eventid IN (SELECT eventid FROM events WHERE userid = :userid);");
		votesQuery.setParameter("userid", userid);
		List<BigInteger> list = ((List<BigInteger>) votesQuery.getResultList());
		if(list.isEmpty() || list.get(0) == null)
			return Optional.empty();
		return Optional.of(list.get(0).intValue());
	}
	
	@Override
	public UserComment createComment(final User commenter, final User receiver, final String comment) {
		
		final UserComment userComment = new UserComment(commenter, receiver, comment);
		em.persist(userComment);
		
		return userComment;
	}

	@Override
	public User create(String username, String firstname, String lastname, String password, Role role)
			throws UserAlreadyExistsException {
		final User user = new User(username, firstname, lastname, password, role, Instant.now());
		em.persist(user);
		return user;
	}

}
