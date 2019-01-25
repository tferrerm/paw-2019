package ar.edu.itba.paw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UpperCaseUserServiceImpl implements UserService {
	
	@Autowired
	private UserDao ud;

	@Override
	public User findById(long userid) {
		final User u = ud.findById(userid);
		u.setUsername(u.getUsername().toUpperCase());
		return u;
	}

}
