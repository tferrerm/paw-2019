package ar.edu.itba.paw.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import ar.edu.itba.paw.webapp.form.NewUserForm;


@Controller
public class EventController extends BaseController {
	
	@Autowired
	private EventService es;

    @Autowired
    private EmailService ems;
	
	@RequestMapping("/home")
	public ModelAndView home()	{
//		ems.joinEventEmail("sswinnen@itba.edu.ar","Juan", "Evento", getLocale());
	    return new ModelAndView("home");
	}

	@RequestMapping("/my-events/{page}")
	public ModelAndView list(@PathVariable("page") final int pageNum)	{
		ModelAndView mav = new ModelAndView("myEvents");
		mav.addObject("events", es.findByUsername(loggedUser().getUsername()));
	    return mav;
	}

    @RequestMapping(value = "/event/{id}")
    public ModelAndView retrieveElement(@PathVariable long id)
    	throws EventNotFoundException {
	    ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", es.findByEventId(id).orElseThrow(EventNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/events/{page}")
    public ModelAndView retrieveEvents( @ModelAttribute("filtersForm") final FiltersForm form,
                                         @PathVariable("page") final long page,
                                         @RequestParam(value = "est" ,required = false) String establishment,
                                         @RequestParam(value = "sport", required = false) String sport,
                                         @RequestParam(value = "org", required = false) String organizer,
                                         @RequestParam(value = "vac", required = false) String vacancies,
                                         @RequestParam(value = "date", required = false) String date) {
        String queryString = buildQueryString(establishment,sport,organizer,vacancies,date);
        ModelAndView mav = new ModelAndView("list");
        mav.addObject("page", page);
        mav.addObject("queryString", queryString);
        mav.addObject("filtersForm",form);
        mav.addObject("lastPageNum", 10);
        return mav;
    }
    
    @RequestMapping(value = "/event/create", method = { RequestMethod.POST })
    public ModelAndView createEvent(
    		@Valid @ModelAttribute("newEventForm") final NewEventForm form,
			final BindingResult errors,
			HttpServletRequest request) {
    	if(errors.hasErrors())
    		return newEvent(form);
    	Event e = es.create(form.getName(), form.getLocation(), form.getDescription(),
    			form.getStartsAt(), form.getEndsAt());
    	return new ModelAndView("redirect:/event/" + e.getEventId());
    	
    }
    
	@RequestMapping("/event/new")
	public ModelAndView newEvent(@ModelAttribute("newEventForm") final NewEventForm form) {
		return new ModelAndView("newEvent");
	}

    @RequestMapping(value = "/events/filter")
    public ModelAndView applyFilter( @ModelAttribute("filtersForm") final FiltersForm form) {
        String establishment = form.getEstablishment();
        String sport = form.getSport();
        String organizer = form.getOrganizer();
        String vacancies = form.getVacancies();
        String date = form.getDate();
        String queryString = buildQueryString(establishment, sport, organizer, vacancies, date);
        return new ModelAndView("redirect:/events/1"+queryString);

    }

    private String buildQueryString(final String establishment, final String sport,
                                    final String organizer, final String vacancies, final String date){
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("?");
        if(establishment != null && !establishment.isEmpty()) { strBuilder.append("est=").append(establishment).append("&"); }
        if(sport != null && !sport.isEmpty()) { strBuilder.append("sport=").append(sport).append("&"); }
        if(organizer != null && !organizer.isEmpty()) { strBuilder.append("org=").append(organizer).append("&"); }
        if(vacancies != null && !vacancies.isEmpty()) { strBuilder.append("vac=").append(vacancies).append("&"); }
        if(date != null && !date.isEmpty()) { strBuilder.append("date=").append(date); }
        else {strBuilder.deleteCharAt(strBuilder.length()-1);}
        return strBuilder.toString();
    }

	@ExceptionHandler({EventNotFoundException.class})
	private ModelAndView eventNotFound() {
		return new ModelAndView("404");
	}



}
