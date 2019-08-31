package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.ClubsFiltersForm;
import ar.edu.itba.paw.webapp.form.CommentForm;

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
	public Response lala() {
		return null;
	}
	
	/*@GET
	@Path("/{id}")
	public Response showClub(@PathParam("id") long clubid,
			@RequestParam(value = "cmt", defaultValue = "1") final int pageNum,
			@ModelAttribute("commentForm") final CommentForm form) throws ClubNotFoundException {
		
		//ModelAndView mav = new ModelAndView("club");
		
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		//mav.addObject("club", club);
		
		List<Pitch> pitches = ps.findByClubId(clubid, 1);
		//mav.addObject("pitches", pitches);
		
		//mav.addObject("past_events_count", cs.countPastEvents(clubid));
		
		//mav.addObject("haveRelationship", loggedUser() != null ? cs.haveRelationship(loggedUser().getUserid(), clubid) : false);

		List<ClubComment> comments = cs.getCommentsByClub(clubid, pageNum);
		//mav.addObject("comments", comments);
		//mav.addObject("commentQty", comments.size());
		//mav.addObject("currCommentPage", pageNum);
		//mav.addObject("maxCommentPage", cs.getCommentsMaxPage(clubid));
		//mav.addObject("totalCommentQty", cs.countByClubComments(clubid));
		//mav.addObject("commentsPageInitIndex", cs.getCommentsPageInitIndex(pageNum));
		
		return null;//mav;
	}
	
	@POST
	@Path("/{id}/comment")
    public Response comment(@PathParam("id") long clubId, 
    		@Valid @ModelAttribute("commentForm") final CommentForm form,
    		final BindingResult errors, HttpServletRequest request) 
    				throws UserNotAuthorizedException, ClubNotFoundException {
		
		if(errors.hasErrors()) {
    		return showClub(clubId, 1, form);
    	}
		
		cs.createComment(loggedUser().getUserid(), clubId, form.getComment());
		
	    return null;//new ModelAndView("redirect:/club/" + clubId);
	}

	@GET
	@Path("/{pageNum}")
	public Response clubs(
			@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form,
			@PathParam("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String clubName,
            @RequestParam(value = "location", required = false) String location) {
		
		String queryString = buildQueryString(clubName, location);
		
		//ModelAndView mav = new ModelAndView("clubList");
		
		//mav.addObject("pageNum", pageNum);
        //mav.addObject("queryString", queryString);
        //mav.addObject("pageInitialIndex", cs.getPageInitialClubIndex(pageNum));
        
        List<Club> clubs = cs.findBy(
				Optional.ofNullable(clubName), 
        		Optional.ofNullable(location),
        		pageNum);
        //mav.addObject("clubs", clubs);
        //mav.addObject("clubQty", clubs.size());
        
        Integer totalClubQty = cs.countFilteredClubs(Optional.ofNullable(clubName), 
        		Optional.ofNullable(location));
        //mav.addObject("totalClubQty", totalClubQty);
        //mav.addObject("lastPageNum", cs.countClubPages(totalClubQty));

		return null;//mav;
	}
	
	@GET
	@Path("/filter")
    public Response applyFilter(@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form) {
    	String name = form.getName();
    	String location = form.getLocation();
        String queryString = buildQueryString(name, location);
        return null;//new ModelAndView("redirect:/clubs/1" + queryString);
    }

    private String buildQueryString(final String name, final String location){
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("?");
	    if(name != null && !name.isEmpty()) {
        	strBuilder.append("name=").append(encodeUriString(name)).append("&");
        }
        if(location != null && !location.isEmpty()) {
        	strBuilder.append("location=").append(encodeUriString(location));
        } else {
        	strBuilder.deleteCharAt(strBuilder.length()-1);
        }
        return strBuilder.toString();
    }
	
//	@ExceptionHandler({ ClubNotFoundException.class })
//	public ModelAndView clubNotFound() {
//		return new ModelAndView("404");
//	}*/
	
}
