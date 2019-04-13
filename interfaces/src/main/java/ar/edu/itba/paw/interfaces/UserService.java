package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

public interface UserService {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	/**
	 * 
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param password
	 * @param role
	 * @param picture
	 * @return
	 * @throws UserAlreadyExistsException If a user with that username existed already
	 */
	public User create(String username, String firstname, String lastname, 
			String password, Role role, byte[] picture) throws UserAlreadyExistsException;

}
