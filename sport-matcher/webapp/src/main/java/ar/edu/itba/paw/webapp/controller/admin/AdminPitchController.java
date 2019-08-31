package ar.edu.itba.paw.webapp.controller.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.webapp.controller.BaseController;

@Path("admin/pitches")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminPitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@Context
	private	UriInfo	uriInfo;
	
	@GET
	@Path("/pitch/jaja")
	public Response oaoa() {
		return null;
	}
	
	/*@DELETE
	@Path("/pitch/{pitchId}/delete")
	public Response deletePitch(
			@PathParam("pitchId") final long pitchId) throws PitchNotFoundException {
		Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
		long clubid = p.getClub().getClubid();
		ps.deletePitch(pitchId);
		return null;//new ModelAndView("redirect:/admin/club/" + clubid);
	}
	
//	@ExceptionHandler({ PitchNotFoundException.class })
//	public ModelAndView pitchNotFoundHandler() {
//		return new ModelAndView("404");
//	}*/

}
