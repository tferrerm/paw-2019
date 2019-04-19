package ar.edu.itba.paw.webapp.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.controller.ClubNotFoundException;

@RequestMapping("/admin")
@Controller
public class ClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	
	@Autowired
	private ClubService cs;
	
	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid) 
			throws ClubNotFoundException {
		
		ModelAndView mav = new ModelAndView("club");
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		return mav;
	}
	
	@RequestMapping("/club/new")
	public ModelAndView newClub() {
		return new ModelAndView("admin/newClub");
	}
	
	@RequestMapping(value = "/club/create", method = { RequestMethod.POST })
	public ModelAndView createClub() {
		Club c = cs.create(loggedUser(), "", "");
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		return new ModelAndView("redirect:/club/" + c.getClubid());
	}
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}

}
