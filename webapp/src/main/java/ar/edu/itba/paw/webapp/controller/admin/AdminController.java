package ar.edu.itba.paw.webapp.controller.admin;

import java.util.List;

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

import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.controller.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;

@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

	@Autowired
	private UserService us;

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
									   @RequestParam(value = "est" ,required = false) String establishment,
									   @RequestParam(value = "sport", required = false) String sport,
									   @RequestParam(value = "org", required = false) String organizer,
									   @RequestParam(value = "vac", required = false) String vacancies,
									   @RequestParam(value = "date", required = false) String date) {
		String queryString = buildAdminQueryString(establishment, sport, organizer, vacancies, date);
		ModelAndView mav = new ModelAndView("admin/index");
		mav.addObject("page", pageNum);
		mav.addObject("queryString", queryString);
		mav.addObject("filtersForm", form);
		mav.addObject("lastPageNum", es.countFutureEventPages());
		mav.addObject("events", es.findFutureEvents(pageNum));
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
		List<User> participants = es.findEventUsers(event.getEventId(), 1);
		mav.addObject("event", event);
		mav.addObject("participant_count", es.countParticipants(event.getEventId()));
		mav.addObject("participants", participants);
		mav.addObject("is_participant", participants.contains(loggedUser()));
		return mav;
	}

	@RequestMapping(value = "/event/{id}/delete", method = { RequestMethod.POST })
	public ModelAndView deleteEvent(@PathVariable long id)
			throws EventNotFoundException {
		es.deleteEvent(id);
		return new ModelAndView("redirect:/admin/events/1");
	}

	private String buildAdminQueryString(final String establishment, final String sport,
									final String organizer, final String vacancies, final String date) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("?");
		if(establishment != null && !establishment.isEmpty()) {
			strBuilder.append("est=").append(establishment).append("&");
		}
		if(sport != null && !sport.isEmpty()) {
			strBuilder.append("sport=").append(sport).append("&");
		}
		if(organizer != null && !organizer.isEmpty()) {
			strBuilder.append("org=").append(organizer).append("&");
		}
		if(vacancies != null && !vacancies.isEmpty()) {
			strBuilder.append("vac=").append(vacancies).append("&");
		}
		if(date != null && !date.isEmpty()) {
			strBuilder.append("date=").append(date);
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
