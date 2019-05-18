package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchPictureService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.form.PitchesFiltersForm;

@Controller
public class PitchController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PitchController.class);
	private static final String DEFAULT_PITCH_PICTURE = "pitch_default.png";
	
	@Autowired
	private PitchService ps;
	
	@Autowired
	private PitchPictureService pps;
	
	@RequestMapping("/pitches/{pageNum}")
	public ModelAndView listPitches(
			@ModelAttribute("pitchesFiltersForm") final PitchesFiltersForm form,
			@PathVariable("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sport", required = false) Sport sport,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "clubname", required = false) String clubName) {
		String sportString = null;
		if(sport != null)
			sportString = sport.toString();
		String queryString = buildQueryString(name, sportString, location, clubName);
		ModelAndView mav = new ModelAndView("pitchesList");
		mav.addObject("page", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("sports", Sport.values());
        mav.addObject("lastPageNum", ps.countFuturePitchPages());
        
        List<Pitch> pitches = ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				Optional.ofNullable(clubName),
				pageNum);
		mav.addObject("pitches", pitches);
		mav.addObject("pitchQty", pitches.size());
		
		Integer totalPitchesQty = ps.countFilteredPitches(Optional.ofNullable(name), 
        		Optional.ofNullable(sport), Optional.ofNullable(location), 
        		Optional.ofNullable(clubName));
        mav.addObject("totalPitchesQty", totalPitchesQty);
        
        mav.addObject("pageInitialIndex", ps.getPageInitialPitchIndex(pageNum));
        
		return mav;
	}
	
	@RequestMapping(value = "/pitches/filter")
    public ModelAndView applyFilter(@ModelAttribute("pitchesFiltersForm") final PitchesFiltersForm form) {
		String name = form.getName();
        String sport = form.getSport();
        String location = form.getLocation();
        String clubName = form.getClubName();
        String queryString = buildQueryString(name, sport, location, clubName);
        return new ModelAndView("redirect:/pitches/1" + queryString);
    }
	
	private String buildQueryString(final String name, final String sport, final String location, final String clubName) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("?");
		if(name != null && !name.isEmpty()) {
			strBuilder.append("name=").append(name).append("&");
		}
		if(sport != null && !sport.isEmpty()) {
			strBuilder.append("sport=").append(sport).append("&");
		}
		if(location != null && !location.isEmpty()) {
			strBuilder.append("location=").append(location).append("&");
		}
		if(clubName != null && !clubName.isEmpty()) {
			strBuilder.append("clubname=").append(clubName);
		} else {
			strBuilder.deleteCharAt(strBuilder.length()-1);
		}
		return strBuilder.toString();
	}
	
	@RequestMapping("/pitch/{pitchId}/picture")
	public void getPitchPicture(@PathVariable("pitchId") long pitchid,
			HttpServletResponse response) {
		Optional<PitchPicture> picOptional = pps.findByPitchId(pitchid);
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		try {
			if(picOptional.isPresent()) {
				response.getOutputStream().write(picOptional.get().getData());
			}
			else {
				LOGGER.debug("Returning default pitch picture");
				IOUtils.copy(new ClassPathResource(DEFAULT_PITCH_PICTURE).getInputStream(), response.getOutputStream());
			}
		}
		catch(IOException e) {
			LOGGER.error("Reading of pitch #{}'s picture failed.", pitchid);
		}
	}
	
	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}

}
