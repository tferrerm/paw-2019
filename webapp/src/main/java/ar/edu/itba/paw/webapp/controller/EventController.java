package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InscriptionDateExceededException;
import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.FiltersForm;
import ar.edu.itba.paw.webapp.form.NewEventForm;


@Controller
public class EventController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
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

    @Autowired
    private UserService us;

    @Autowired
    private TournamentService ts;


	@RequestMapping("/home")
	public ModelAndView home()	{
		ModelAndView mav = new ModelAndView("home");

		String[] scheduleDaysHeader = es.getScheduleDaysHeader();
		if(loggedUser() != null) {
			List<Event> upcomingEvents = es.findFutureUserInscriptions(loggedUser().getUserid(), true);
			Event[][] myEvents = es.convertEventListToSchedule(upcomingEvents,
					DAY_LIMIT, MAX_EVENTS_PER_DAY);

			mav.addObject("myEvents", myEvents);
			mav.addObject("scheduleHeaders", scheduleDaysHeader);

			boolean noParticipations = upcomingEvents.isEmpty();
			mav.addObject("noParticipations", noParticipations);
		}

	    return mav;
	}


	@RequestMapping("/my-events/{page}")
	public ModelAndView list(@PathVariable("page") final int pageNum)	{
		ModelAndView mav = new ModelAndView("myEvents");
		if (loggedUser() != null) {
			long userid = loggedUser().getUserid();
			List<Event> futureEvents = es.findByOwner(true, userid, pageNum);
			List<Event> pastEvents = es.findByOwner(false, userid, pageNum);

			mav.addObject("future_events", futureEvents);
			mav.addObject("past_events", pastEvents);

			mav.addObject("page", pageNum);
			int futureEventQty = es.countByOwner(true, userid);
			int pastEventQty = es.countByOwner(false, userid);

			boolean countFutureOwnedPages = (futureEventQty > pastEventQty);
	        mav.addObject("lastPageNum", es.countUserOwnedPages(countFutureOwnedPages, userid));
		}
	    return mav;
	}


	@RequestMapping("/history/{page}")
	public ModelAndView historyList(@PathVariable("page") final int pageNum)	{
		ModelAndView mav = new ModelAndView("history");
		if (loggedUser() != null) {
			long loggedUserId = loggedUser().getUserid();
			List<Event> events = es.findPastUserInscriptions(loggedUserId, pageNum);
			mav.addObject("past_participations", events);
			mav.addObject("eventQty", events.size());

			mav.addObject("page", pageNum);
	        mav.addObject("lastPageNum", es.countUserInscriptionPages(false, loggedUserId));
	        mav.addObject("totalEventQty", es.countByUserInscriptions(false, loggedUserId));
	        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
		}
		return mav;
	}


    @RequestMapping(value = "/event/{id}")
    public ModelAndView retrieveElement(@PathVariable long id,
    		@RequestParam(value = "eventFullError", required = false) boolean eventFullError,
    		@RequestParam(value = "alreadyJoinedError", required = false) boolean alreadyJoinedError,
    		@RequestParam(value = "userBusyError", required = false) boolean userBusyError)
    	throws EventNotFoundException, ClubNotFoundException {

    	Optional<TournamentEvent> tournamentEvent = ts.findTournamentEventById(id);
    	if(tournamentEvent.isPresent()) {
    		return new ModelAndView("redirect:/tournament/" + tournamentEvent.get().getTournament().getTournamentid() + "/event/" + id);
    	}

    	ModelAndView mav = new ModelAndView("event");

    	Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	    List<Inscription> inscriptions = event.getInscriptions();
	    User current = loggedUser();

        mav.addObject("event", event);

        mav.addObject("participant_count", inscriptions.size());
        mav.addObject("inscriptions", inscriptions);

        boolean isParticipant = false;
        if (loggedUser() != null) {
	        for(Inscription i : inscriptions) {
	        	if(i.getInscriptedUser().equals(current))
	        		isParticipant = true;
	        }
        }
        mav.addObject("is_participant", isParticipant);

        mav.addObject("has_started", Instant.now().isAfter(event.getStartsAt()));
        mav.addObject("has_ended", Instant.now().isAfter(event.getEndsAt()));

        mav.addObject("vote_balance", es.getVoteBalance(event.getEventId()));
        if (loggedUser() != null) {
        	mav.addObject("user_vote", es.getUserVote(event.getEventId(), current.getUserid()));
        }

        mav.addObject("eventFullError", eventFullError);
        mav.addObject("alreadyJoinedError", alreadyJoinedError);
        mav.addObject("userBusyError", userBusyError);

        mav.addObject("isOwner", loggedUser() != null ? event.getOwner().getUserid() == loggedUser().getUserid() : false);

        return mav;
    }


    @RequestMapping(value = "/event/{id}/join", method = { RequestMethod.POST })
    public ModelAndView joinEvent(@PathVariable long id)
    	throws EventNotFoundException, DateInPastException {
	    Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	    ModelAndView mav = new ModelAndView("redirect:/event/" + id);
	    if (loggedUser() != null) {
		    try {
		    	es.joinEvent(loggedUser().getUserid(), id);

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
	    }
        return mav;
    }


    @RequestMapping(value = "/event/{id}/leave", method = { RequestMethod.POST })
    public ModelAndView leaveEvent(@PathVariable long id) throws DateInPastException {
    	if (loggedUser() != null) {
    		es.leaveEvent(id, loggedUser().getUserid());
    	}
        return new ModelAndView("redirect:/event/" + id);
    }


    @RequestMapping(value = "/event/{eventId}/kick-user/{userId}", method = { RequestMethod.POST })
    public ModelAndView kickUserFromEvent(
    		@PathVariable("eventId") long eventid,
    		@PathVariable("userId") long kickedUserId)
    		throws UserNotAuthorizedException, EventNotFoundException, UserNotFoundException, DateInPastException {
    	if (loggedUser() != null) {
	    	Event event = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	User kicked = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
	    	es.kickFromEvent(loggedUser(), kickedUserId, event);
	    	ems.youWereKicked(kicked, event, LocaleContextHolder.getLocale());
    	}
    	return new ModelAndView("redirect:/event/" + eventid);
    }


    @RequestMapping(value = "/events/{pageNum}")
    public ModelAndView retrieveEvents(@ModelAttribute("filtersForm") final FiltersForm form,
                                     @PathVariable("pageNum") final int pageNum,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "establishment", required = false) String clubName,
                                     @RequestParam(value = "sport", required = false) Sport sport,
                                     @RequestParam(value = "vacancies", required = false) String vacancies,
                                     @RequestParam(value = "date", required = false) String date) {
    	String sportName = "";
    	if(sport != null)
    		sportName = sport.toString();

        String queryString = buildQueryString(name, clubName, sportName, vacancies, date);
        ModelAndView mav = new ModelAndView("list");

        Integer vac = tryInteger(vacancies);
    	Instant dateInst = tryInstantStartOfDay(date, TIME_ZONE);
    	if(vac == null && vacancies != null && !vacancies.isEmpty())
    		mav.addObject("invalid_number_format", true);
    	if(dateInst == null && date != null && !date.isEmpty())
    		mav.addObject("invalid_date_format", true);

        mav.addObject("page", pageNum);
        mav.addObject("queryString", queryString);
        mav.addObject("sports", Sport.values());

        List<Event> events = es.findBy(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst), pageNum);

        mav.addObject("events", events);
        mav.addObject("eventQty", events.size());

        Integer totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst));
        mav.addObject("totalEventQty", totalEventQty);
        mav.addObject("lastPageNum", es.countEventPages(totalEventQty));
        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
        mav.addObject("currentDate", LocalDate.now());
        mav.addObject("aWeekFromNow", LocalDate.now().plus(7, ChronoUnit.DAYS));

        return mav;
    }


	@RequestMapping("/pitch/{pitchId}")
	public ModelAndView seePitch(
			@PathVariable("pitchId") long id,
			@ModelAttribute("newEventForm") final NewEventForm form) throws PitchNotFoundException {

		ModelAndView mav = new ModelAndView("pitch");

		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
		mav.addObject("scheduleHeaders", es.getScheduleDaysHeader());
		mav.addObject("minHour", MIN_HOUR);
		mav.addObject("maxHour", MAX_HOUR);
		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
		mav.addObject("schedule", es.convertEventListToSchedule(es.findCurrentEventsInPitch(id), MIN_HOUR, MAX_HOUR, DAY_LIMIT));
		mav.addObject("currentDate", LocalDate.now());
		mav.addObject("currentDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		mav.addObject("aWeekFromNow", LocalDate.now().plus(7, ChronoUnit.DAYS));
		mav.addObject("aWeekFromNowDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plus(7, ChronoUnit.DAYS));

		return mav;
	}


    @RequestMapping(value = "/pitch/{pitchId}/event/create", method = { RequestMethod.POST })
    public ModelAndView createEvent(
    		@PathVariable("pitchId") long pitchId,
    		@Valid @ModelAttribute("newEventForm") final NewEventForm form,
			final BindingResult errors,
			HttpServletRequest request) throws PitchNotFoundException {

    	Integer mp = tryInteger(form.getMaxParticipants());
    	Integer sa = tryInteger(form.getStartsAtHour());
    	Integer ea = tryInteger(form.getEndsAtHour());
    	Instant date = tryInstantStartOfDay(form.getDate(), TIME_ZONE);
    	Instant inscriptionEndDate = tryDateTimeToInstant(form.getInscriptionEndDate(), TIME_ZONE);
    	if(mp == null)
    		errors.rejectValue("maxParticipants", "wrong_int_format");
    	if(sa == null)
    		errors.rejectValue("startsAtHour", "wrong_int_format");
    	if(ea == null)
    		errors.rejectValue("endsAtHour", "wrong_int_format");
    	if(date == null)
    		errors.rejectValue("date", "wrong_date_format");
    	if(inscriptionEndDate == null)
    		errors.rejectValue("inscriptionEndDate", "wrong_date_format");

    	if(errors.hasErrors()) {
    		return seePitch(pitchId, form);
    	}

    	Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
    	Event ev = null;
    	try {
	    	ev = es.create(form.getName(), loggedUser(), p, form.getDescription(),
	    			mp, date, sa, ea, inscriptionEndDate);
    	} catch(EndsBeforeStartsException e) {
    		return eventCreationError("ends_before_starts", pitchId, form);
    	} catch(DateInPastException e) {
    		return eventCreationError("event_in_past", pitchId, form);
    	} catch(MaximumDateExceededException e) {
    		return eventCreationError("date_exceeded", pitchId, form);
    	} catch(EventOverlapException e) {
    		return eventCreationError("event_overlap", pitchId, form);
    	} catch(HourOutOfRangeException e) {
    		return eventCreationError("hour_out_of_range", pitchId, form);
    	} catch(InscriptionDateInPastException e) {
    		return eventCreationError("inscription_date_in_past", pitchId, form);
    	} catch(InscriptionDateExceededException e) {
    		return eventCreationError("inscription_date_exceeded", pitchId, form);
    	}
    	return new ModelAndView("redirect:/event/" + ev.getEventId());
    }


    private ModelAndView eventCreationError(String error, long pitchId, NewEventForm form)
    	throws PitchNotFoundException {
    	ModelAndView mav = seePitch(pitchId, form);
		mav.addObject(error, true);
		return mav;
    }


    @RequestMapping(value = "/event/{id}/delete", method = { RequestMethod.POST })
	public ModelAndView deleteEvent(@PathVariable final long id)
			throws EventNotFoundException, UserNotAuthorizedException, DateInPastException {
    	if (loggedUser() != null) {
				Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
				List<User> inscriptedUsers = event.getInscriptions().stream().map(i -> i.getInscriptedUser()).collect(Collectors.toList());
				es.cancelEvent(event, loggedUser().getUserid());
				for(User inscriptedUser : inscriptedUsers) {
					if(inscriptedUser != event.getOwner())
						ems.eventCancelled(inscriptedUser, event, LocaleContextHolder.getLocale());
				}
				LOGGER.debug("Deleted event with id {}", id);
    	}
		return new ModelAndView("redirect:/events/1");
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
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(true, ev, loggedUser().getUserid());
    	}
    	return new ModelAndView("redirect:/event/" + eventid);
    }


    @RequestMapping(value = "/event/{eventId}/downvote", method = { RequestMethod.POST })
    public ModelAndView downvote(@PathVariable("eventId") final long eventid)
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(false, ev, loggedUser().getUserid());
    	}
    	return new ModelAndView("redirect:/event/" + eventid);
    }


    private String buildQueryString(final String name, final String establishment, final String sport,
                                    final String vacancies, final String date){
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("?");
	    if(name != null && !name.isEmpty()) {
        	strBuilder.append("name=").append(encodeUriString(name)).append("&");
        }
        if(establishment != null && !establishment.isEmpty()) {
        	strBuilder.append("establishment=").append(encodeUriString(establishment)).append("&");
        }
        if(sport != null && !sport.isEmpty()) {
        	strBuilder.append("sport=").append(encodeUriString(sport)).append("&");
        }
        if(vacancies != null && !vacancies.isEmpty()) {
        	strBuilder.append("vacancies=").append(encodeUriString(vacancies)).append("&");
        }
        if(date != null && !date.isEmpty()) {
        	strBuilder.append("date=").append(encodeUriString(date));
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


	@ExceptionHandler({ UserNotFoundException.class })
	private ModelAndView userNotFound() {
		return new ModelAndView("404");
	}

}
