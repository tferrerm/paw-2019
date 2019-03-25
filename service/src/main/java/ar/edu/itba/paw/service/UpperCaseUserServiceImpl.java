package ar.edu.itba.paw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Service
public class UpperCaseUserServiceImpl implements UserService {
	
	@Autowired
	private UserDao ud;

	@Override
	public Optional<User> findById(long userid) {
		final User u = ud.findById(userid).get();
		//u.setUsername(u.getUsername().toUpperCase());
		return Optional.ofNullable(u);
	}
	
	@Override
	public Optional<User> findByUsername(final String username) {
		return ud.findByUsername(username);
	}

	@Override
	public User create(String username, String password, Role role, byte[] picture) {
		return ud.create(username, password, role);
	}

}
