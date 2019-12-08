package ar.edu.itba.paw.webapp.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.dto.PitchDto;
import ar.edu.itba.paw.webapp.dto.form.PitchForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@Path("admin/clubs/{clubId}/pitches")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminPitchController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminPitchController.class);
	
	@Autowired
	private PitchService ps;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private FormValidator validator;
	
	@Context
	private	UriInfo	uriInfo;
	
	@POST
	public Response createPitch(@PathParam("clubId") final long clubId,
			@FormDataParam("name") String name,	@FormDataParam("sport") String sport,
			@FormDataParam("picture") InputStream picture)
				throws ClubNotFoundException, PictureProcessingException, FormValidationException {

		validator.validate(new PitchForm().withName(name).withSport(sport));
		
		//final MultipartFile pitchPicture = form.getPitchPicture();
		Club c = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
		Sport s = Sport.valueOf(sport);
		Pitch pitch;
		
		try {
			byte[] pictureBytes = {};
			if(picture != null)
				pictureBytes = IOUtils.toByteArray(picture);
			pitch = ps.create(c, name, s, pictureBytes);
			LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		} catch(IOException e) {
			LOGGER.error("Error reading pitch picture {}", e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
		final URI uri = uriInfo.getBaseUriBuilder().path("/pitches/" + pitch.getPitchid()).build();
		return Response.created(uri).entity(PitchDto.ofPitch(pitch)).build();
	}
	
	@DELETE
	@Path("/{pitchId}")
	public Response deletePitch(@PathParam("clubId") final long clubid,
			@PathParam("pitchId") final long pitchId) throws ClubNotFoundException, PitchNotFoundException {
		cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		final Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
		ps.deletePitch(p.getPitchid());
		return Response.status(Status.NO_CONTENT).build();
	}
	
//	@ExceptionHandler({ PitchNotFoundException.class })
//	public ModelAndView pitchNotFoundHandler() {
//		return new ModelAndView("404");
//	}

}
