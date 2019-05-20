package ar.edu.itba.paw.webapp.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@RequestMapping("/admin")
@Controller
public class AdminPitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@RequestMapping(value = "/pitch/{pitchId}/delete", method = { RequestMethod.POST })
	public ModelAndView deletePitch(
			@PathVariable("pitchId") final long pitchId) throws PitchNotFoundException {
		Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
		long clubid = p.getClub().getClubid();
		ps.deletePitch(pitchId);
		return new ModelAndView("redirect:/admin/club/" + clubid);
	}
	
	@ExceptionHandler({ PitchNotFoundException.class })
	public ModelAndView pitchNotFoundHandler() {
		return new ModelAndView("404");
	}

}
