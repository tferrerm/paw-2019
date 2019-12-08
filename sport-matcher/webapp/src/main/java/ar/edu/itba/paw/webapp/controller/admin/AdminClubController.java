package ar.edu.itba.paw.webapp.controller.admin;

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

//
//	@GET
//	@Path("/{pageNum}")
//	public ModelAndView clubs(
//			@PathParam("pageNum") int pageNum) {

//		
//		ModelAndView mav = new ModelAndView("admin/clubList");
//        
//        List<Club> clubs = cs.findBy(
//				Optional.ofNullable(clubName), 
//        		Optional.ofNullable(location), 
//        		pageNum);
//        
//        Integer totalClubQty = cs.countFilteredClubs(Optional.ofNullable(clubName), 
//        		Optional.ofNullable(location));
//        mav.addObject("totalClubQty", totalClubQty);
//        mav.addObject("lastPageNum", cs.countClubPages(totalClubQty));
//        
//		return mav;
//	}
//
    @POST
	public Response createClub(@FormDataParam("name") final String name,
			@FormDataParam("location") final String location) throws FormValidationException {

    	validator.validate(new ClubForm().withName(name).withLocation(location));
		
		final Club c = cs.create(name, location);
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());

		final URI uri = uriInfo.getBaseUriBuilder().path("/clubs/" + c.getClubid()).build();
		return Response.created(uri).entity(ClubDto.ofClub(c)).build();
	}
//

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
