package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.User;

public interface UserDao {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public User create(String username, String password);

}
