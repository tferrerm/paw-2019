package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import ar.edu.itba.paw.webapp.form.NewClubForm;
import ar.edu.itba.paw.webapp.form.NewEventForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.webapp.controller.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController extends BaseController {

	@Autowired
	private ClubService cs;

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
		String queryString = buildAdminQueryString(establishment,sport,organizer,vacancies,date);
		ModelAndView mav = new ModelAndView("admin/index");
		mav.addObject("page", pageNum);
		mav.addObject("queryString", queryString);
		mav.addObject("filtersForm",form);
		mav.addObject("lastPageNum", es.countFutureEventPages());
		mav.addObject("events", es.findFutureEvents(pageNum));
		return mav;
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

	@RequestMapping(value = "/event/{id}/delete")
	public ModelAndView deleteEvent(@PathVariable long id)
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

	private String buildAdminQueryString(final String establishment, final String sport,
									final String organizer, final String vacancies, final String date){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("?");
		if(establishment != null && !establishment.isEmpty()) { strBuilder.append("est=").append(establishment).append("&"); }
		if(sport != null && !sport.isEmpty()) { strBuilder.append("sport=").append(sport).append("&"); }
		if(organizer != null && !organizer.isEmpty()) { strBuilder.append("org=").append(organizer).append("&"); }
		if(vacancies != null && !vacancies.isEmpty()) { strBuilder.append("vac=").append(vacancies).append("&"); }
		if(date != null && !date.isEmpty()) { strBuilder.append("date=").append(date); }
		else {strBuilder.deleteCharAt(strBuilder.length()-1); }
		return strBuilder.toString();
	}

}
