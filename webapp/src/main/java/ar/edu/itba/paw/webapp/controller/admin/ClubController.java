package ar.edu.itba.paw.webapp.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.controller.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.NewClubForm;

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
	public ModelAndView clubs(@PathVariable("pageNum") int pageNum) {
		ModelAndView mav = new ModelAndView("admin/clubList");
		List<Club> clubs = cs.findAll(pageNum); //Cambiar por query de clubes
		mav.addObject("clubs", clubs);
		return mav;
	}
	
	@RequestMapping("/club/new")
	public ModelAndView newClub(@ModelAttribute("newClubForm") final NewClubForm form) {
		return new ModelAndView("admin/newClub");
	}

	@RequestMapping(value = "/club/create", method = { RequestMethod.POST })
	public ModelAndView createClub(@Valid @ModelAttribute("newClubForm") final NewClubForm form, final BindingResult errors) {
		Club c = cs.create(form.getName(), form.getLocation());
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		return new ModelAndView("redirect:/admin/clubs");
	}
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}

}
