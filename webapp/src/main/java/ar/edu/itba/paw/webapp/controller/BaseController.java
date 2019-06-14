package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

public class BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
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
	
    public Integer tryInteger(String str) {
    	Integer i = null;
    	try {
    		Integer.parseInt(str);
    	} catch(NumberFormatException e) {
    		return null;
    	}
    	return i;
    }
    
    public Instant tryInstant(String str) {
    	Instant i = null;
    	try {
    		i = LocalDate.parse(str).atStartOfDay(ZoneId.of(TIME_ZONE)).toInstant();
    	} catch(DateTimeParseException e) {
    		return null;
    	}
    	return i;
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
