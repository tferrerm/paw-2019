package ar.edu.itba.paw.service;

import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User findById(long userid) {
		return new User(1, "Pepe", "12345678");
	}

}
