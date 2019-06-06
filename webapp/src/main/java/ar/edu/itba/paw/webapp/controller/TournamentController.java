package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exception.*;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import ar.edu.itba.paw.webapp.form.NewEventForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Controller
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);

    @RequestMapping(value = "/tournaments/{pageNum}")
    public ModelAndView retrieveTournaments(@ModelAttribute("tournamentFiltersForm") final FiltersForm form,
                                         @PathVariable("pageNum") final int pageNum,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "est", required = false) String establishment,
                                         @RequestParam(value = "sport", required = false) Sport sport,
                                         @RequestParam(value = "vac", required = false) String vacancies,
                                         @RequestParam(value = "date", required = false) String date) {
    	String sportName = "";
    	if(sport != null)
    		sportName = sport.toString();

        String queryString = buildQueryString(name, establishment, sportName, vacancies, date);
        ModelAndView mav = new ModelAndView("tournaments");
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

	@ExceptionHandler({ EventNotFoundException.class })
	private ModelAndView eventNotFound() {
		return new ModelAndView("404");
	}

}
