package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController extends BaseController {
	
	@RequestMapping("/home")
	public ModelAndView home()	{
		return new ModelAndView("home");
	}

	@RequestMapping("/list")
	public ModelAndView list()	{
		return new ModelAndView("list");
	}

	@RequestMapping(value = "/event/{id}")
	public ModelAndView retrieveElement(@PathVariable long id) {
		ModelAndView mav = new ModelAndView("event");
		/*Query*/
		return mav;
	}



}
