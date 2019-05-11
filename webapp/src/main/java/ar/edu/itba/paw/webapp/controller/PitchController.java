package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Sport;

@Controller
public class PitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@RequestMapping("/pitch/{pitchId}")
	public ModelAndView seePitch(@PathVariable long id) throws PitchNotFoundException {
		ModelAndView mav = new ModelAndView("pitch");
		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
		return mav;
	}
	
	@RequestMapping("/pitches/{pageNum}")
	public ModelAndView listPitches(
			@PathVariable("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sport", required = false) Sport sport,
			@RequestParam(value = "location", required = false) String location) {
		ModelAndView mav = new ModelAndView("listPitches");
		mav.addObject("pitches", ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				pageNum));
		return mav;
	}
	
	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}

}
