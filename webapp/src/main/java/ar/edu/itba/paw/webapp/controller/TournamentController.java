package ar.edu.itba.paw.webapp.controller;

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
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;


@Controller
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
	
	@Autowired
	private TournamentService ts;
	
	@Autowired
	private ClubService cs;

    @RequestMapping(value = "/tournament/{tournamentId}")
    public ModelAndView retrieveTournaments(@PathVariable long tournamentId) {
        ModelAndView mav = new ModelAndView("tournament");
        mav.addObject("tournament",  null);
        return mav;
    }

    @RequestMapping(value = "/tournaments/{pageNum}")
    public ModelAndView retrieveTournaments(
			@ModelAttribute("tournamentFiltersForm") final FiltersForm form,
			@PathVariable("pageNum") final int pageNum,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "est", required = false) String clubName,
			@RequestParam(value = "sport", required = false) Sport sport,
			@RequestParam(value = "vac", required = false) String vacancies,
			@RequestParam(value = "date", required = false) String date) {
    	String sportName = "";
    	if(sport != null)
    		sportName = sport.toString();

        String queryString = buildQueryString(name, clubName, sportName, vacancies, date);
        ModelAndView mav = new ModelAndView("tournamentList");
        mav.addObject("page", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("sports", Sport.values());
        mav.addObject("lastPageNum", "");
//
//        try {
//	        List<Event> events = es.findByWithInscriptions(true, Optional.ofNullable(name),
//	        		Optional.ofNullable(establishment), Optional.ofNullable(sport), Optional.empty(),
//	        		Optional.ofNullable(vacancies), Optional.ofNullable(date), pageNum);
//
//	        mav.addObject("events", events);
//	        mav.addObject("eventQty", events.size());
//
//	        Integer totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name),
//	        		Optional.ofNullable(establishment), Optional.ofNullable(sport), Optional.empty(),
//	        		Optional.ofNullable(vacancies), Optional.ofNullable(date));
//	        mav.addObject("totalEventQty", totalEventQty);
//        } catch(InvalidDateFormatException e) {
//        	mav.addObject("invalid_date_format", true);
//        	return mav;
//        } catch(InvalidVacancyNumberException e) {
//        	mav.addObject("invalid_number_format", true);
//        	return mav;
//        }
//
        
        return mav;
    }


    @RequestMapping(value = "/tournaments/filter")
    public ModelAndView applyFilter(@ModelAttribute("filtersForm") final FiltersForm form) {
    	String name = form.getName();
        String establishment = form.getEstablishment();
        String sport = form.getSport();
        String vacancies = form.getVacancies();
        String date = form.getDate();
        String queryString = buildQueryString(name, establishment, sport, vacancies, date);
        return new ModelAndView("redirect:/events/1" + queryString);
    }

    private String buildQueryString(final String name, final String establishment, final String sport,
                                    final String vacancies, final String date){
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("?");
	    if(name != null && !name.isEmpty()) {
        	strBuilder.append("name=").append(name).append("&");
        }
        if(establishment != null && !establishment.isEmpty()) {
        	strBuilder.append("est=").append(establishment).append("&");
        }
        if(sport != null && !sport.isEmpty()) {
        	strBuilder.append("sport=").append(sport).append("&");
        }
        if(vacancies != null && !vacancies.isEmpty()) {
        	strBuilder.append("vac=").append(vacancies).append("&");
        }
        if(date != null && !date.isEmpty()) {
        	strBuilder.append("date=").append(date);
        } else {
        	strBuilder.deleteCharAt(strBuilder.length()-1);
        }
        return strBuilder.toString();
    }

    @RequestMapping(value = "tournament/{tournamentId}/team/{teamId}/join")
    public ModelAndView joinTeam(@PathVariable long tournamentId, @PathVariable long teamId) {
        ModelAndView mav = new ModelAndView("tournament");
        mav.addObject("tournament",  null);
        return mav;
    }

	@ExceptionHandler({ EventNotFoundException.class })
	private ModelAndView eventNotFound() {
		return new ModelAndView("404");
	}

}
