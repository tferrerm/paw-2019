package ar.edu.itba.paw.webapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.form.PitchesFiltersForm;

@Controller
public class PitchController extends BaseController {
	
	@Autowired
	private PitchService ps;
	
	@RequestMapping("/pitch/{pitchId}")
	public ModelAndView seePitch(@PathVariable("pitchId") long id) throws PitchNotFoundException {
		ModelAndView mav = new ModelAndView("pitch");
		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
		// Armar matriz calendario con los datos extraidos de la DB para ese pitch
		boolean[][] calendar = new boolean[14][7];
		calendar[0][1] = true;
		mav.addObject("calendar", calendar);
		return mav;
	}
	
	@RequestMapping("/pitches/{pageNum}")
	public ModelAndView listPitches(
			@ModelAttribute("pitchesFiltersForm") final PitchesFiltersForm form,
			@PathVariable("pageNum") int pageNum,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sport", required = false) Sport sport,
			@RequestParam(value = "location", required = false) String location) {
		String sportString = null;
		if(sport != null)
			sportString = sport.toString();
		String queryString = buildQueryString(name, sportString, location);
		ModelAndView mav = new ModelAndView("pitchesList");
		mav.addObject("page", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("sports", Sport.values());
		mav.addObject("pitches", ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				pageNum));
		return mav;
	}
	
	@RequestMapping(value = "/pitches/filter")
    public ModelAndView applyFilter(@ModelAttribute("pitchesFiltersForm") final PitchesFiltersForm form) {
        String name = form.getName();
        String sport = form.getSport();
        String location = form.getLocation();
        String queryString = buildQueryString(name, sport, location);
        return new ModelAndView("redirect:/pitches/1" + queryString);
    }
	
	private String buildQueryString(final String name, final String sport, final String location) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("?");
		if(name != null && !name.isEmpty()) {
			strBuilder.append("name=").append(name).append("&");
		}
		if(sport != null && !sport.isEmpty()) {
			strBuilder.append("sport=").append(sport).append("&");
		}
		if(location != null && !location.isEmpty()) {
			strBuilder.append("location=").append(location);
		}else {
			strBuilder.deleteCharAt(strBuilder.length()-1);
		}
		return strBuilder.toString();
	}
	
	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}

}
