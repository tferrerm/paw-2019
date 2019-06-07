package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private ProfilePictureService pps;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";

	@Override
	public Optional<User> findById(final long userid) {
		if(userid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ud.findById(userid);
	}

	@Override
	public Optional<User> findByUsername(final String username) {
		return ud.findByUsername(username);
	}
	
	@Override
	public int countVotesReceived(final long userid) {
		if(userid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ud.countVotesReceived(userid).orElse(0);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public User create(String username, String firstname, String lastname, 
			String password, Role role, byte[] picture)
			throws UserAlreadyExistsException, PictureProcessingException {
		User user = ud.create(username.toLowerCase(), firstname, lastname, password, role);
		if(picture != null) {
			pps.create(user, picture);
		}
		return user;
	}

}
