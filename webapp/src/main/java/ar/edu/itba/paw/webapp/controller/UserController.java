package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService us;
	
	@RequestMapping("/")
	public ModelAndView helloWorld() {
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("greeting", "PAW");
		return mav;
	}
}
