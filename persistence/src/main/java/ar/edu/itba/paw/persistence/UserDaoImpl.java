package ar.edu.itba.paw.persistence;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Override
	public User findById(long userid) {
		return new User(1, "Pepe", "12345678");
	}

}
