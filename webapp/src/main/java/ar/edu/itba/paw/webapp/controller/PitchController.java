package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.form.NewEventForm;
import ar.edu.itba.paw.webapp.form.PitchesFiltersForm;

@Controller
public class PitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@Autowired
	private EventService es;
	
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
		mav.addObject("pitches", ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				Optional.ofNullable(clubName),
				pageNum));
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
	
	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}

}
