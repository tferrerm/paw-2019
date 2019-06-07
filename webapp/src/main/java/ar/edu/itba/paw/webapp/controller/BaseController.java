package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

public class BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@ModelAttribute("loggedUser")
	public User loggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			LOGGER.debug("Returning null as logged user");
			return null;
		}
		LOGGER.debug("Auth is: {}", auth.isAuthenticated());
		if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
			return null;
		}
		final Optional<User> user = us.findByUsername(auth.getName());
		return user.get();
	}
	
	/*@ExceptionHandler({ IllegalArgumentException.class })
	private ModelAndView illegalIdOrPageNumber() {
		return new ModelAndView("404");
	}
	
	@ExceptionHandler({ UserNotAuthorizedException.class })
	private ModelAndView illegalActionForUser() {
		return new ModelAndView("403");
	}
	
    @ExceptionHandler({ Exception.class })
	private ModelAndView generalExceptionHandler() {
		return new ModelAndView("oops");
	}*/

}
