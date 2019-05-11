package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.webapp.form.NewClubForm;
import ar.edu.itba.paw.webapp.form.NewEventForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.controller.ClubNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class ClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	
	@Autowired
	private ClubService cs;
	
	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid) 
			throws ClubNotFoundException {
		
		ModelAndView mav = new ModelAndView("admin/clubs");
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		return mav;
	}

	@RequestMapping("/clubs/{pageNum}")
	public ModelAndView clubs(@PathVariable("pageNum") long pageNum) {
		ModelAndView mav = new ModelAndView("admin/clubList");
		List<Club> clubs = new ArrayList<>(); //Cambiar por query de clubes
		mav.addObject("clubs",clubs);
		return mav;
	}
	
	@RequestMapping("/club/new")
	public ModelAndView newClub(@ModelAttribute("newClubForm") final NewClubForm form) {
		return new ModelAndView("admin/newClub");
	}

	@RequestMapping(value = "/club/create", method = { RequestMethod.POST })
	public ModelAndView createClub(@Valid @ModelAttribute("newClubForm") final NewClubForm form, final BindingResult errors) {
		Club c = cs.create(loggedUser(), form.getName(), form.getLocation());
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		return new ModelAndView("redirect:/admin/clubs");
	}
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}

}
