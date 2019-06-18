package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.ClubsFiltersForm;
import ar.edu.itba.paw.webapp.form.CommentForm;

@Controller
public class ClubController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private PitchService ps;
	
	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid,
			@RequestParam(value = "cmt", defaultValue = "1") final int pageNum,
			@ModelAttribute("commentForm") final CommentForm form) throws ClubNotFoundException {
		
		ModelAndView mav = new ModelAndView("club");
		
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		
		List<Pitch> pitches = ps.findByClubId(clubid, 1);
		mav.addObject("pitches", pitches);
		
		mav.addObject("past_events_count", cs.countPastEvents(clubid));
		
		mav.addObject("haveRelationship", loggedUser() != null ? cs.haveRelationship(loggedUser().getUserid(), clubid) : false);

		List<ClubComment> comments = cs.getCommentsByClub(clubid, pageNum);
		mav.addObject("comments", comments);
		mav.addObject("commentQty", comments.size());
		mav.addObject("currCommentPage", pageNum);
		mav.addObject("maxCommentPage", cs.getCommentsMaxPage(clubid));
		mav.addObject("totalCommentQty", cs.countByClubComments(clubid));
		mav.addObject("commentsPageInitIndex", cs.getCommentsPageInitIndex(pageNum));
		
		return mav;
	}
	
	@RequestMapping(value = "/club/{clubId}/comment", method = { RequestMethod.POST })
    public ModelAndView comment(@PathVariable("clubId") long clubId, 
    		@Valid @ModelAttribute("commentForm") final CommentForm form,
    		final BindingResult errors, HttpServletRequest request) 
    				throws UserNotAuthorizedException, ClubNotFoundException {
		
		if(errors.hasErrors()) {
    		return showClub(clubId, 1, form);
    	}
		
		cs.createComment(loggedUser().getUserid(), clubId, form.getComment());
		
	    return new ModelAndView("redirect:/club/" + clubId);
	}

	@RequestMapping("/clubs/{pageNum}")
	public ModelAndView clubs(
			@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form,
			@PathVariable("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String clubName,
            @RequestParam(value = "location", required = false) String location) {
		
		String queryString = buildQueryString(clubName, location);
		
		ModelAndView mav = new ModelAndView("clubList");
		
		mav.addObject("pageNum", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("pageInitialIndex", cs.getPageInitialClubIndex(pageNum));
        
        List<Club> clubs = cs.findBy(
				Optional.ofNullable(clubName), 
        		Optional.ofNullable(location),
        		pageNum);
        mav.addObject("clubs", clubs);
        mav.addObject("clubQty", clubs.size());
        
        Integer totalClubQty = cs.countFilteredClubs(Optional.ofNullable(clubName), 
        		Optional.ofNullable(location));
        mav.addObject("totalClubQty", totalClubQty);
        mav.addObject("lastPageNum", cs.countClubPages(totalClubQty));

		return mav;
	}
	
	@RequestMapping(value = "/clubs/filter")
    public ModelAndView applyFilter(@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form) {
    	String name = form.getName();
    	String location = form.getLocation();
        String queryString = buildQueryString(name, location);
        return new ModelAndView("redirect:/clubs/1" + queryString);
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
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}
	
}
