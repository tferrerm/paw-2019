package ar.edu.itba.paw.interfaces;

import java.io.IOException;
import java.util.Optional;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import exception.UserAlreadyExistsException;

public interface UserService {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public User create(String username, String password, Role role, byte[] picture)
		throws UserAlreadyExistsException, IOException;

}
