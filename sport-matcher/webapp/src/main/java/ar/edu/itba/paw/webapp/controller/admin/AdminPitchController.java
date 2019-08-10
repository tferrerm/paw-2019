package ar.edu.itba.paw.webapp.controller.admin;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@Path("admin/pitches")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminPitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@Context
	private	UriInfo	uriInfo;
	
	@DELETE
	@Path("/pitch/{pitchId}/delete")
	public ModelAndView deletePitch(
			@PathVariable("pitchId") final long pitchId) throws PitchNotFoundException {
		Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
		long clubid = p.getClub().getClubid();
		ps.deletePitch(pitchId);
		return new ModelAndView("redirect:/admin/club/" + clubid);
	}
	
//	@ExceptionHandler({ PitchNotFoundException.class })
//	public ModelAndView pitchNotFoundHandler() {
//		return new ModelAndView("404");
//	}

}
