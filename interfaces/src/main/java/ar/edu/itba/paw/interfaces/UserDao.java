package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

public interface UserDao {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public User create(String username, String firstname, String lastname, 
			String password, Role role) throws UserAlreadyExistsException;

}
