package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public Optional<User> findById(final long userid) {
		return ud.findById(userid);
	}

	@Override
	public Optional<User> findByUsername(final String username) {
		return ud.findByUsername(username);
	}

	@Transactional
	@Override
	public User create(String username, String password, Role role, byte[] picture)
		throws UserAlreadyExistsException {
		User user = ud.create(username, password, role);
		if(picture != null) {
			pps.create(user.getUserid(), picture);
		}
		return user;
	}

}
