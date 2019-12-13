package ar.edu.itba.paw.webapp.controller.admin;

import java.net.URI;

import javax.ws.rs.Consumes;
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

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.dto.ClubDto;
import ar.edu.itba.paw.webapp.dto.form.ClubForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;

@Path("admin/clubs")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminClubController.class);
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private FormValidator validator;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createClub(@FormDataParam("clubForm") ClubForm form)
			throws FormValidationException {
    	if(form == null) {
    		return Response.status(Status.BAD_REQUEST).build();
    	}
    	
    	validator.validate(form);
		
		final Club c = cs.create(form.getName(), form.getLocation());
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());

		final URI uri = uriInfo.getBaseUriBuilder().path("/clubs/" + c.getClubid()).build();
		return Response.created(uri).entity(ClubDto.ofClub(c)).build();
	}

    @DELETE
	@Path("/{id}")
	public Response deleteClub(@PathParam("id") final long clubid) 
		throws ClubNotFoundException {
		cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		cs.deleteClub(clubid);
		return Response.status(Status.NO_CONTENT).build();
	}
//	
////	@ExceptionHandler({ ClubNotFoundException.class })
////	public ModelAndView clubNotFound() {
////		return new ModelAndView("404");
////	}

}
