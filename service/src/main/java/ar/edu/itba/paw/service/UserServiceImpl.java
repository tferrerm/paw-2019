package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao ud;

	@Override
	public Optional<User> findById(final long userid) {
		return ud.findById(userid);
	}

	@Override
	public Optional<User> findByUsername(final String username) {
		return ud.findByUsername(username);
	}

	@Override
	public User create(String username, String password) {
		return ud.create(username, password);
	}

}
