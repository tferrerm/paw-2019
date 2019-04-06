package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;


@Controller
public class EventController extends BaseController {

    @Autowired
    private EmailService es;
	
	@RequestMapping("/home")
	public ModelAndView home()	{
//		es.joinEventEmail("sswinnen@itba.edu.ar","Juan", "Evento", getLocale());
	    return new ModelAndView("home");
	}

	@RequestMapping("/my-events/{page}")
	public ModelAndView list(@PathVariable("page") final int pageNum)	{

	    return new ModelAndView("myEvents");
	}

    @RequestMapping(value = "/event/{id}")
    public ModelAndView retrieveElement(@PathVariable long id) {
	    ModelAndView mav = new ModelAndView("event");
        /*Query*/
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





}
