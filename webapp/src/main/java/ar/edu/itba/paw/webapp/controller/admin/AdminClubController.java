package ar.edu.itba.paw.webapp.controller.admin;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.ClubsFiltersForm;
import ar.edu.itba.paw.webapp.form.NewClubForm;
import ar.edu.itba.paw.webapp.form.NewPitchForm;

@RequestMapping("/admin")
@Controller
public class AdminClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminClubController.class);
	
	@Autowired
	private ClubService cs;

	@Autowired
	private PitchService ps;

	@RequestMapping("/club/{clubId}")
	public ModelAndView showClub(@PathVariable("clubId") long clubid, @ModelAttribute("newPitchForm") final NewPitchForm form)
			throws ClubNotFoundException {

		List<Pitch> pitches = ps.findByClubId(clubid, 1);
		ModelAndView mav = new ModelAndView("admin/club");
		mav.addObject("newPitchForm", form);
		mav.addObject("sports", Sport.values());
		mav.addObject("pitches", pitches);
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		return mav;
	}

	@RequestMapping("/clubs/{pageNum}")
	public ModelAndView clubs(@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form,
			@PathVariable("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String clubName,
            @RequestParam(value = "location", required = false) String location) {
		
		String queryString = buildQueryString(clubName, location);
		
		ModelAndView mav = new ModelAndView("admin/clubList");
		
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
        return new ModelAndView("redirect:/admin/clubs/1" + queryString);
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
	
	@RequestMapping("/club/new")
	public ModelAndView newClub(@ModelAttribute("newClubForm") final NewClubForm form) {
		return new ModelAndView("admin/newClub");
	}

	@RequestMapping(value = "/club/create", method = { RequestMethod.POST })
	public ModelAndView createClub(
			@Valid @ModelAttribute("newClubForm") final NewClubForm form,
			final BindingResult errors,
			HttpServletRequest request) {
		
		Club c = cs.create(form.getName(), form.getLocation());
		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		
		return new ModelAndView("redirect:/admin/club/" + c.getClubid());
	}

	@RequestMapping(value = "/club/{clubId}/pitch/create", method = { RequestMethod.POST })
	public ModelAndView createPitch(
			@Valid @ModelAttribute("newPitchForm") final NewPitchForm form,
			final BindingResult errors,
			HttpServletRequest request,
			@PathVariable("clubId") final long clubId) 
					throws ClubNotFoundException {
		
		Sport sport = null;
		try {
			sport = Sport.valueOf(form.getSport());
		} catch(IllegalArgumentException e) {
			LOGGER.warn("Unable to convert sport to enum");
			errors.rejectValue("sport", "sport_not_in_list");
		}
		
		if(errors.hasErrors()) {
			return showClub(clubId, form);
		}
		
		final MultipartFile pitchPicture = form.getPitchPicture();
		Club c = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
		
		try {
			byte[] picture = pitchPicture.getBytes();
			ps.create(c, form.getName(), sport, picture);
			LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
		} catch(PictureProcessingException | IOException e) {
			LOGGER.error("Error reading pitch picture {}", pitchPicture.getOriginalFilename());
			ModelAndView mav = showClub(c.getClubid(), form);
			mav.addObject("fileErrorMessage", pitchPicture.getOriginalFilename());
			return mav;
		}
		
		return new ModelAndView("redirect:/admin/club/" + clubId);
	}
	
	@RequestMapping(value = "/club/{clubId}/delete", method = { RequestMethod.POST })
	public ModelAndView deleteClub(@PathVariable("clubId") final long clubid) 
		throws ClubNotFoundException {
		cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		cs.deleteClub(clubid);
		return new ModelAndView("redirect:/admin/clubs/1");
	}
	
	@ExceptionHandler({ ClubNotFoundException.class })
	public ModelAndView clubNotFound() {
		return new ModelAndView("404");
	}

}
