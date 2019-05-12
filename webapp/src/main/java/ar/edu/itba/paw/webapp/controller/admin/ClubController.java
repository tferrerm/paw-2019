package ar.edu.itba.paw.webapp.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.form.NewPitchForm;
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

	@Autowired
	private PitchService ps;

	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid, @ModelAttribute("newPitchForm") final NewPitchForm form)
			throws ClubNotFoundException {

		List<Pitch> pitches = ps.findByClubId(clubid, 1);
		Sport [] sports = Sport.values();
		ModelAndView mav = new ModelAndView("admin/club");
		mav.addObject("newPitchForm",form);
		mav.addObject("sports",sports);
		mav.addObject("pitches",pitches);
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		return mav;
	}

	@RequestMapping("/clubs/{pageNum}")
	public ModelAndView clubs(@PathVariable("pageNum") int pageNum) {
		ModelAndView mav = new ModelAndView("admin/clubList");
		List<Club> clubs = cs.findAll(pageNum);
		mav.addObject("clubs", clubs);
		return mav;
	}
	
	@RequestMapping("/club/new")
	public ModelAndView newClub(@ModelAttribute("newClubForm") final NewClubForm form) {
		return new ModelAndView("admin/newClub");
	}

	@RequestMapping(value = "/club/create", method = { RequestMethod.POST })
	public ModelAndView createClub(@Valid @ModelAttribute("newClubForm") final NewClubForm form, final BindingResult errors) {
		Club c = cs.create(loggedUser().getUserid(),form.getName(), form.getLocation());
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		return new ModelAndView("redirect:/admin/clubs");
	}

	@RequestMapping(value = "/club/{clubId}/pitch/create", method = { RequestMethod.POST })
	public ModelAndView createPitch(@Valid @ModelAttribute("newPitchForm") final NewPitchForm form,
									@PathVariable("clubId") final long clubId, final BindingResult errors)
			throws ClubNotFoundException {

		Club c = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
		try {
			ps.create(c,form.getName(),Sport.valueOf(form.getSport()));
		} catch(Exception e) {
			LOGGER.debug("Unable to create pitch");
		}
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		return new ModelAndView("redirect:/admin/club/"+clubId);
	}
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}

}
