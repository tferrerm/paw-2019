package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Repository
public class UserHibernateDao implements UserDao {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<User> findById(long userid) {
		return Optional.of(em.find(User.class, userid));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		final TypedQuery<User> query = em.createQuery("FROM User AS u WHERE u.username LIKE :username",
				User.class);
		query.setParameter("username", username);
		query.setMaxResults(1);
		return query.getResultList().stream().findFirst();
	}

	@Override
	public Optional<Integer> countVotesReceived(long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User create(String username, String firstname, String lastname, String password, Role role)
			throws UserAlreadyExistsException {
		final User user = new User(username, firstname, lastname, password, role, Instant.now());
		em.persist(user);
		return user;
	}

}
