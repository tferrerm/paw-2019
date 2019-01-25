package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;

@Controller
public class UserController {
	
	@Qualifier("upperCaseUserServiceImpl")
	@Autowired
	private UserService us;
	
	@RequestMapping("/user/{userId}")
	public ModelAndView userProfile(@PathVariable("userId") long userid) {
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("greeting", us.findById(userid).getUsername());
		return mav;
	}
}
