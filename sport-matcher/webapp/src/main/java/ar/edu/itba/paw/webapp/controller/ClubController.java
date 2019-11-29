package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.webapp.dto.ClubCollectionDto;
import ar.edu.itba.paw.webapp.dto.ClubDto;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;

@Path("clubs")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class ClubController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private PitchService ps;
	
	@GET
	@Path("/{id}")
	public Response showClub(@PathParam("id") long clubid/*,
			@RequestParam(value = "cmt", defaultValue = "1") final int pageNum,*/
			/*@ModelAttribute("commentForm") final CommentForm form*/) throws ClubNotFoundException {
		
		final Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		
		//List<Pitch> pitches = ps.findByClubId(clubid, 1);
		//mav.addObject("pitches", pitches);
		
		//mav.addObject("past_events_count", cs.countPastEvents(clubid));
		
		//mav.addObject("haveRelationship", loggedUser() != null ? cs.haveRelationship(loggedUser().getUserid(), clubid) : false);

		//List<ClubComment> comments = cs.getCommentsByClub(clubid, pageNum);
		//mav.addObject("comments", comments);
		//mav.addObject("commentQty", comments.size());
		//mav.addObject("currCommentPage", pageNum);
		//mav.addObject("maxCommentPage", cs.getCommentsMaxPage(clubid));
		//mav.addObject("totalCommentQty", cs.countByClubComments(clubid));
		//mav.addObject("commentsPageInitIndex", cs.getCommentsPageInitIndex(pageNum));
		
		return Response
				.status(Status.OK)
				.entity(ClubDto.ofClub(club))
				.build();
	}
	
//	@POST
//	@Path("/{id}/comment")
//    public Response comment(@PathParam("id") long clubId, 
//    		@Valid @ModelAttribute("commentForm") final CommentForm form,
//    		final BindingResult errors, HttpServletRequest request) 
//    				throws UserNotAuthorizedException, ClubNotFoundException {
//		
//		if(errors.hasErrors()) {
//    		return showClub(clubId, 1, form);
//    	}
//		
//		cs.createComment(loggedUser().getUserid(), clubId, form.getComment());
//		
//	    return null;//new ModelAndView("redirect:/club/" + clubId);
//	}

	@GET
	@Path("/")
	public Response clubs(
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("name") String clubName,
            @QueryParam("location") String location) {
        
        final List<Club> clubs = cs.findBy(
				Optional.ofNullable(clubName), 
        		Optional.ofNullable(location),
        		pageNum);
        
        int totalClubQty = cs.countFilteredClubs(
        		Optional.ofNullable(clubName), 
        		Optional.ofNullable(location));
        int clubPages = cs.countClubPages(totalClubQty);

		return Response
				.status(Status.OK)
				.entity(ClubCollectionDto.ofClubs(
						clubs.stream().map(ClubDto::ofClub).collect(Collectors.toList()),
						totalClubQty, clubPages))
				.build();
	}
	
//	@ExceptionHandler({ ClubNotFoundException.class })
//	public ModelAndView clubNotFound() {
//		return new ModelAndView("404");
//	}*/
	
}
