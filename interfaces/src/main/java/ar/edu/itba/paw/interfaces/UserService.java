package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;

public interface UserService {
	
	public Optional<User> findById(final long userid);
	
	public Optional<User> findByUsername(final String username);
	
	public int countVotesReceived(final long userid);
	
	public UserComment createComment(final long commenterid, final long receiverid, final String comment)
			throws UserNotAuthorizedException;
	
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
			String password, Role role, byte[] picture)
			throws UserAlreadyExistsException, PictureProcessingException;

}
