package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchService;

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
	
	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}

}
