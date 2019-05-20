package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.ClubsFiltersForm;

@Controller
public class ClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private PitchService ps;
	
	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid) 
			throws ClubNotFoundException {
		
		ModelAndView mav = new ModelAndView("club");
		
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		
		List<Pitch> pitches = ps.findByClubId(clubid, 1);
		mav.addObject("pitches", pitches);
		
		mav.addObject("past_events_count", cs.countPastEvents(clubid));
		
		return mav;
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
        mav.addObject("lastPageNum", cs.countClubPages());
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
        	strBuilder.append("name=").append(name).append("&");
        }
        if(location != null && !location.isEmpty()) {
        	strBuilder.append("location=").append(location);
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
