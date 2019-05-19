package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventInPastException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.InvalidDateFormatException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import ar.edu.itba.paw.webapp.form.NewEventForm;


@Controller
public class EventController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;
	private static final int DAY_LIMIT = 7;
	private static final int MAX_EVENTS_PER_DAY = 14;
	
	@Autowired
	private EventService es;

    @Autowired
    private EmailService ems;

    @Autowired
    private PitchService ps;

	@RequestMapping("/home")
	public ModelAndView home()	{
//		ems.joinEventEmail("sswinnen@itba.edu.ar","Juan", "Evento", LocaleContextHolder.getLocale());
		ModelAndView mav = new ModelAndView("home");
		String[] scheduleDaysHeader = es.getScheduleDaysHeader();
		List<Event> upcomingEvents = es.findByUserInscriptions(true, loggedUser().getUserid(), 1);
		Event[][] myEvents = es.convertEventListToSchedule(upcomingEvents, DAY_LIMIT, MAX_EVENTS_PER_DAY);
		mav.addObject("myEvents", myEvents);
		mav.addObject("scheduleHeaders", scheduleDaysHeader);
	    return mav;
	}

	@RequestMapping("/my-events/{page}")
	public ModelAndView list(@PathVariable("page") final int pageNum)	{
		ModelAndView mav = new ModelAndView("myEvents");
		long userid = loggedUser().getUserid();
		mav.addObject("future_events", es.findByOwner(true, userid, pageNum));
		mav.addObject("past_events", es.findByOwner(false, userid, pageNum));
	    return mav;
	}

	@RequestMapping("/history/{page}")
	public ModelAndView historyList(@PathVariable("page") final int pageNum)	{
		ModelAndView mav = new ModelAndView("history");
		mav.addObject("past_participations", es.findByUserInscriptions(false, loggedUser().getUserid(), pageNum));
	    return mav;
	}

    @RequestMapping(value = "/event/{id}")
    public ModelAndView retrieveElement(@PathVariable long id,
    		@RequestParam(value = "eventFullError", required = false) boolean eventFullError,
    		@RequestParam(value = "alreadyJoinedError", required = false) boolean alreadyJoinedError,
    		@RequestParam(value = "userBusyError", required = false) boolean userBusyError)
    	throws EventNotFoundException, ClubNotFoundException {
	    
    	ModelAndView mav = new ModelAndView("event");
	    
    	Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	    List<User> participants = es.findEventUsers(event.getEventId(), 1);
	    User current = loggedUser();
	    
        mav.addObject("event", event);
        
        mav.addObject("participant_count", es.countParticipants(event.getEventId()));
        mav.addObject("participants", participants);
        mav.addObject("is_participant", participants.contains(current));
        mav.addObject("has_started", Instant.now().isAfter(event.getStartsAt()));
        mav.addObject("has_ended", Instant.now().isAfter(event.getEndsAt()));
        
        mav.addObject("vote_balance", es.getVoteBalance(event.getEventId()));
        mav.addObject("user_vote", es.getUserVote(event.getEventId(), current.getUserid()));
        
        mav.addObject("eventFullError", eventFullError);
        mav.addObject("alreadyJoinedError", alreadyJoinedError);
        mav.addObject("userBusyError", userBusyError);

        return mav;
    }

    @RequestMapping(value = "/event/{id}/join", method = { RequestMethod.POST })
    public ModelAndView joinEvent(@PathVariable long id)
    	throws EventNotFoundException {
	    Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	    ModelAndView mav = new ModelAndView("redirect:/event/" + id);
	    try {
	    	es.joinEvent(loggedUser(), event);

	    } catch(EventFullException e) {
	    	mav.addObject("eventFullError", true);
	    	return mav;
	    } catch(UserAlreadyJoinedException e) {
	    	LOGGER.debug("User {} tried to join event {}, but had already joined", loggedUser(), event);
	    	mav.addObject("alreadyJoinedError", true);
	    	return mav;
	    } catch(UserBusyException e) {
	    	mav.addObject("userBusyError", true);
	    	return mav;
	    }
        return mav;
    }

    @RequestMapping(value = "/event/{id}/leave", method = { RequestMethod.POST })
    public ModelAndView leaveEvent(@PathVariable long id)
    	throws EventNotFoundException {
	    Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	    es.leaveEvent(loggedUser(), event);
        return new ModelAndView("redirect:/event/" + id);
    }

    @RequestMapping(value = "/event/{eventId}/kick-user/{userId}", method = { RequestMethod.POST })
    public ModelAndView kickUserFromEvent(
    		@PathVariable("eventId") long eventid,
    		@PathVariable("userId") long kickedUserId)
    				throws UserNotAuthorizedException, EventNotFoundException {
    	Event event = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	es.kickFromEvent(loggedUser(), kickedUserId, event);
    	return new ModelAndView("redirect:/event/" + eventid);
    }

    @RequestMapping(value = "/events/{pageNum}")
    public ModelAndView retrieveEvents(@ModelAttribute("filtersForm") final FiltersForm form,
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
        ModelAndView mav = new ModelAndView("list");
        mav.addObject("page", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("sports", Sport.values());
        mav.addObject("lastPageNum", es.countFutureEventPages());
        Integer vacanciesNum = null;
        if(vacancies != null)
        	vacanciesNum = Integer.valueOf(vacancies);
        
        List<Event> events = es.findByWithInscriptions(true, Optional.ofNullable(name), 
        		Optional.ofNullable(establishment), Optional.ofNullable(sport), 
        		Optional.ofNullable(vacanciesNum), pageNum);

        mav.addObject("events", events);
        mav.addObject("eventQty", events.size());
        
        Integer totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name), 
        		Optional.ofNullable(establishment), Optional.ofNullable(sport), 
        		Optional.ofNullable(vacanciesNum));
        mav.addObject("totalEventQty", totalEventQty);
        
        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
        
        return mav;
    }

	@RequestMapping("/pitch/{pitchId}")
	public ModelAndView seePitch(
			@PathVariable("pitchId") long id,
			@ModelAttribute("newEventForm") final NewEventForm form) throws PitchNotFoundException {
		ModelAndView mav = new ModelAndView("pitch");
		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
		List<Event> pitchEvents = es.findCurrentEventsInPitch(id);
		boolean[][] schedule = es.convertEventListToSchedule(pitchEvents, MIN_HOUR, MAX_HOUR, DAY_LIMIT);
		String[] scheduleDaysHeader = es.getScheduleDaysHeader();
		mav.addObject("scheduleHeaders", scheduleDaysHeader);
		mav.addObject("minHour", MIN_HOUR);
		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
		mav.addObject("schedule", schedule);
		return mav;
	}

    @RequestMapping(value = "/pitch/{pitchId}/event/create", method = { RequestMethod.POST })
    public ModelAndView createEvent(
    		@PathVariable("pitchId") long pitchId,
    		@Valid @ModelAttribute("newEventForm") final NewEventForm form,
			final BindingResult errors,
			HttpServletRequest request) throws PitchNotFoundException {

    	if(errors.hasErrors()) {
    		return seePitch(pitchId, form);
    	}

    	Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
    	Event ev = null;
    	try {
	    	ev = es.create(form.getName(), loggedUser(), p, form.getDescription(),
	    			form.getMaxParticipants(), form.getDate(), form.getStartsAtHour(),
	    			form.getEndsAtHour());
    	} catch(InvalidDateFormatException e) {
    		return eventCreationError("invalid_date_format", pitchId, form);
    	} catch(EndsBeforeStartsException e) {
    		return eventCreationError("ends_before_starts", pitchId, form);
    	} catch(EventInPastException e) {
    		return eventCreationError("event_in_past", pitchId, form);
    	} catch(MaximumDateExceededException e) {
    		return eventCreationError("date_exceeded", pitchId, form);
    	} catch(EventOverlapException e) {
    		return eventCreationError("event_overlap", pitchId, form);
    	}
    	return new ModelAndView("redirect:/event/" + ev.getEventId());
    }

    private ModelAndView eventCreationError(String error, long pitchId, NewEventForm form)
    	throws PitchNotFoundException {
    	ModelAndView mav = seePitch(pitchId, form);
		mav.addObject(error, true);
		return mav;
    }

    @RequestMapping(value = "/events/filter")
    public ModelAndView applyFilter(@ModelAttribute("filtersForm") final FiltersForm form) {
    	String name = form.getName();
        String establishment = form.getEstablishment();
        String sport = form.getSport();
        String vacancies = form.getVacancies();
        String date = form.getDate();
        String queryString = buildQueryString(name, establishment, sport, vacancies, date);
        return new ModelAndView("redirect:/events/1" + queryString);
    }
    
    @RequestMapping(value = "/event/{eventId}/upvote", method = { RequestMethod.POST })
    public ModelAndView upvote(@PathVariable("eventId") final long eventid) 
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	es.vote(true, ev, loggedUser().getUserid());
    	return new ModelAndView("redirect:/event/" + eventid);
    }
    
    @RequestMapping(value = "/event/{eventId}/downvote", method = { RequestMethod.POST })
    public ModelAndView downvote(@PathVariable("eventId") final long eventid) 
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	es.vote(false, ev, loggedUser().getUserid());
    	return new ModelAndView("redirect:/event/" + eventid);
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

	@ExceptionHandler({ PitchNotFoundException.class })
	private ModelAndView pitchNotFound() {
		return new ModelAndView("404");
	}
	
	@ExceptionHandler({ EventNotFinishedException.class })
	private ModelAndView eventNotFinished() {
		return new ModelAndView("404");
	}

}
