package ar.edu.itba.paw.webapp.controller.admin;

import java.time.Instant;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;

@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {
	
	private static final String TIME_ZONE = "America/Buenos_Aires";

	@Autowired
	private EventService es;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	
	@RequestMapping(value = "/")
	public ModelAndView adminHome() {
		return new ModelAndView("redirect:/admin/events/1");
	}

	
	@RequestMapping(value = "/events/{pageNum}")
	public ModelAndView retrieveEvents(@ModelAttribute("filtersForm") final FiltersForm form,
									   @PathVariable("pageNum") final int pageNum,
									   @RequestParam(value = "establishment", required = false) String clubName,
									   @RequestParam(value = "sport", required = false) Sport sport,
									   @RequestParam(value = "organizer", required = false) String organizer,
									   @RequestParam(value = "vacancies", required = false) String vacancies,
									   @RequestParam(value = "date", required = false) String date) {
		
		String sportName = "";
    	if(sport != null)
    		sportName = sport.toString();
		String queryString = buildAdminQueryString(clubName, sportName, organizer, vacancies, date);
		ModelAndView mav = new ModelAndView("admin/index");
		
	   	Integer vac = tryInteger(vacancies);
    	Instant dateInst = tryInstantStartOfDay(date, TIME_ZONE);
    	if(vac == null && vacancies != null && !vacancies.isEmpty())
    		mav.addObject("invalid_number_format", true);
    	if(dateInst == null && date != null && !date.isEmpty())
    		mav.addObject("invalid_date_format", true);
    	
		mav.addObject("page", pageNum);
		mav.addObject("queryString", queryString);
		mav.addObject("sports", Sport.values());
		
		List<Event> events = es.findBy(false, Optional.empty(), Optional.ofNullable(clubName), 
        		Optional.ofNullable(sport), Optional.ofNullable(organizer), 
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst), pageNum);
		mav.addObject("events", events);
		mav.addObject("eventQty", events.size());
		
		Integer totalEventQty = es.countFilteredEvents(false, Optional.empty(), 
				Optional.ofNullable(clubName), Optional.ofNullable(sport), 
				Optional.ofNullable(organizer), Optional.ofNullable(vac),
				Optional.ofNullable(dateInst));
		
        mav.addObject("totalEventQty", totalEventQty);
		mav.addObject("lastPageNum", es.countEventPages(totalEventQty));
        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
        
		return mav;
	}
	
	
	@RequestMapping(value = "/events/filter")
    public ModelAndView applyFilter(@ModelAttribute("filtersForm") final FiltersForm form) {
        String establishment = form.getEstablishment();
        String sport = form.getSport();
        String organizer = form.getOrganizer();
        String vacancies = form.getVacancies();
        String date = form.getDate();
        String queryString = buildAdminQueryString(establishment, sport, organizer, vacancies, date);
        return new ModelAndView("redirect:/admin/events/1" + queryString);
    }

	
	@RequestMapping(value = "/event/{id}")
	public ModelAndView retrieveElement(@PathVariable long id)
			throws EventNotFoundException {
		ModelAndView mav = new ModelAndView("admin/adminEvent");
		Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
		List<Inscription> inscriptions = event.getInscriptions();
		mav.addObject("event", event);
		mav.addObject("participant_count", inscriptions.size());
		mav.addObject("inscriptions", inscriptions);
		return mav;
	}

	
	@RequestMapping(value = "/event/{id}/delete", method = { RequestMethod.POST })
	public ModelAndView deleteEvent(@PathVariable final long id)
			throws EventNotFoundException, DateInPastException {
		es.findByEventId(id).orElseThrow(EventNotFoundException::new);
		es.deleteEvent(id);
		LOGGER.debug("Deleted event with id {}", id);
		return new ModelAndView("redirect:/admin/events/1");
	}

	
	private String buildAdminQueryString(final String establishment, final String sport,
									final String organizer, final String vacancies, final String date) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("?");
		if(establishment != null && !establishment.isEmpty()) {
			strBuilder.append("establishment=").append(encodeString(establishment)).append("&");
		}
		if(sport != null && !sport.isEmpty()) {
			strBuilder.append("sport=").append(encodeString(sport)).append("&");
		}
		if(organizer != null && !organizer.isEmpty()) {
			strBuilder.append("organizer=").append(encodeString(organizer)).append("&");
		}
		if(vacancies != null && !vacancies.isEmpty()) {
			strBuilder.append("vacancies=").append(encodeString(vacancies)).append("&");
		}
		if(date != null && !date.isEmpty()) {
			strBuilder.append("date=").append(encodeString(date));
		}
		else {
			strBuilder.deleteCharAt(strBuilder.length()-1);
		}
		return strBuilder.toString();
	}
	
	
	@ExceptionHandler({ EventNotFoundException.class })
	public ModelAndView eventNotFound() {
		return new ModelAndView("404");
	}

}
