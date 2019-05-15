package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

public class BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute("loggedUser")
	public User loggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			LOGGER.warn("Returning null as logged user");
			return null;
		}
		LOGGER.debug("Auth is: {}", auth.isAuthenticated());
		if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
			return null;
		}
		final Optional<User> user = us.findByUsername(auth.getName());
//		user.isPresent((u) -> (LOGGER.debug("Currently logged user is {}", user.get().getId())));
		return user.get();
	}
	
	@ExceptionHandler({ IllegalArgumentException.class })
	private ModelAndView illegalIdOrPageNumber() {
		return new ModelAndView("404");
	}
	
//    @ExceptionHandler({ Exception.class })
//	private ModelAndView generalExceptionHandler() {
//		return new ModelAndView("oops");
//	}

}
