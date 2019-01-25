package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

@Controller
public class UserController {
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@RequestMapping("/user/{userId}")
	public ModelAndView userProfile(@PathVariable("userId") long userid) 
	throws UserNotFoundException {
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("greeting", us.findById(userid)
				.orElseThrow(UserNotFoundException::new).getUsername());
		return mav;
	}
	
	@RequestMapping("/create")
	public ModelAndView create(
			@RequestParam(value = "username", required = true) final String username) {
		final User u = us.create(username, "12345678");
		return new ModelAndView("redirect:/user/" + u.getUserid());
	}
}
