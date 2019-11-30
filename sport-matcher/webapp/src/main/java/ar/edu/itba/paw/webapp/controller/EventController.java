package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EndsBeforeStartsException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.EventOverlapException;
import ar.edu.itba.paw.exception.HourOutOfRangeException;
import ar.edu.itba.paw.exception.InscriptionDateExceededException;
import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.exception.MaximumDateExceededException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.form.EventForm;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("events")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class EventController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;
	
	@Context
	private	UriInfo	uriInfo;

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

//    @GET
//	@Path("/home") // SACAR
//	public Response home()	{
//		//ModelAndView mav = new ModelAndView("home");
//
//		String[] scheduleDaysHeader = es.getScheduleDaysHeader();
//		if(loggedUser() != null) {
//			List<Event> upcomingEvents = es.findFutureUserInscriptions(loggedUser().getUserid(), true);
//			List<List<Event>> myEvents = es.convertEventListToSchedule(upcomingEvents);
//
//			//mav.addObject("myEvents", myEvents);
//			//mav.addObject("scheduleHeaders", scheduleDaysHeader);
//
//			boolean noParticipations = upcomingEvents.isEmpty();
//			//mav.addObject("noParticipations", noParticipations);
//		}
//
//	    return null;//mav;
//	}

//    @GET
//	@Path("/my-events/{page}") // SACAR
//	public Response list(@PathParam("page") final int pageNum)	{
//		//ModelAndView mav = new ModelAndView("myEvents");
//		if (loggedUser() != null) {
//			long userid = loggedUser().getUserid();
//			List<Event> futureEvents = es.findByOwner(true, userid, pageNum);
//			List<Event> pastEvents = es.findByOwner(false, userid, pageNum);
//
//			//mav.addObject("future_events", futureEvents);
//			//mav.addObject("past_events", pastEvents);
//
//			//mav.addObject("page", pageNum);
//			int futureEventQty = es.countByOwner(true, userid);
//			int pastEventQty = es.countByOwner(false, userid);
//
//			boolean countFutureOwnedPages = (futureEventQty > pastEventQty);
//	        //mav.addObject("lastPageNum", es.countUserOwnedPages(countFutureOwnedPages, userid));
//		}
//	    return null;//mav;
//	}

//    @GET
//	@RequestMapping("/history/{page}")
//	public Response historyList(@PathParam("page") final int pageNum)	{
//		//ModelAndView mav = new ModelAndView("history");
//		if (loggedUser() != null) {
//			long loggedUserId = loggedUser().getUserid();
//			List<Event> events = es.findPastUserInscriptions(loggedUserId, pageNum);
////			mav.addObject("past_participations", events);
////			mav.addObject("eventQty", events.size());
////
////			mav.addObject("page", pageNum);
////	        mav.addObject("lastPageNum", es.countUserInscriptionPages(false, loggedUserId));
////	        mav.addObject("totalEventQty", es.countByUserInscriptions(false, loggedUserId));
////	        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
//		}
//		return null;//mav;
//	}

    //@GET
    //@Path("/{id}")
    //public Response retrieveElement(@PathParam("id") long id)
    		//@RequestParam(value = "eventFullError", required = false) boolean eventFullError,
    		//@RequestParam(value = "alreadyJoinedError", required = false) boolean alreadyJoinedError,
    		//@RequestParam(value = "userBusyError", required = false) boolean userBusyError)
    //	throws EventNotFoundException, ClubNotFoundException {

    //	Optional<TournamentEvent> tournamentEvent = ts.findTournamentEventById(id);
    //	if(tournamentEvent.isPresent()) {
    //		return null;//new ModelAndView("redirect:/tournament/" + tournamentEvent.get().getTournament().getTournamentid() + "/event/" + id);
    //	}

    	//ModelAndView mav = new ModelAndView("event");

    //	Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
	//    List<Inscription> inscriptions = event.getInscriptions();
	//    User current = loggedUser();

//        mav.addObject("event", event);
//
//        mav.addObject("participant_count", inscriptions.size());
//        mav.addObject("inscriptions", inscriptions);

    //    boolean isParticipant = false;
    //    if (loggedUser() != null) {
	//        for(Inscription i : inscriptions) {
	//        	if(i.getInscriptedUser().equals(current))
	//        		isParticipant = true;
	//        }
    //    }
//        mav.addObject("is_participant", isParticipant);
//
//        mav.addObject("has_ended", Instant.now().isAfter(event.getEndsAt()));
//
//        mav.addObject("vote_balance", es.getVoteBalance(event.getEventId()));
    //    if (loggedUser() != null) {
    //    	//mav.addObject("user_vote", es.getUserVote(event.getEventId(), current.getUserid()));
    //    }

//        mav.addObject("eventFullError", eventFullError);
//        mav.addObject("alreadyJoinedError", alreadyJoinedError);
//        mav.addObject("userBusyError", userBusyError);
//
//        mav.addObject("isOwner", loggedUser() != null ? event.getOwner().getUserid() == loggedUser().getUserid() : false);
//        mav.addObject("now", Instant.now());

    //   return null;//mav;
    //}

    @POST
    @RequestMapping("/{id}/join")
    public Response joinEvent(@PathParam("id") long id)
    	throws EntityNotFoundException, DateInPastException, EventFullException, UserBusyException {
	    /* HARDCODEADO HARDCODED InscriptionClosedException */
	    
	    if (loggedUser() != null) {
		    es.joinEvent(loggedUser().getUserid(), id);
	    }
        return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/event/{id}/leave")
    public Response leaveEvent(@PathParam("id") long id) throws DateInPastException, EntityNotFoundException {
    	if (loggedUser() != null) {
    		es.leaveEvent(id, loggedUser().getUserid());
    	}
        return Response.status(Status.NO_CONTENT).build();
    }

    @POST
    @Path("/event/{id}/kick-user/{userId}")
    public Response kickUserFromEvent(
    		@PathParam("id") long eventid,
    		@PathParam("userId") long kickedUserId)
    		throws UserNotAuthorizedException, EventNotFoundException, UserNotFoundException, DateInPastException {
    	if (loggedUser() != null) {
	    	Event event = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	User kicked = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
	    	es.kickFromEvent(loggedUser(), kickedUserId, event);
	    	ems.youWereKicked(kicked, event, LocaleContextHolder.getLocale());
    	}
    	return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    public Response retrieveEvents(@QueryParam("pageNum") @DefaultValue("1") final int pageNum,
                                     @QueryParam("name") String name,
                                     @QueryParam("club") String clubName,
                                     @QueryParam("sport") Sport sport,
                                     @QueryParam("vacancies") String vacancies,
                                     @QueryParam("date") String date) {

        Integer vac = tryInteger(vacancies);
    	Instant dateInst = tryInstantStartOfDay(date, TIME_ZONE);
    	if(vac == null && vacancies != null && !vacancies.isEmpty())
    		System.out.println("HOLA");
    	if(dateInst == null && date != null && !date.isEmpty())
    		System.out.println("HOLA");

        List<Event> events = es.findBy(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst), pageNum);

        int totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name),
        		Optional.ofNullable(clubName), Optional.ofNullable(sport), Optional.empty(),
        		Optional.ofNullable(vac), Optional.ofNullable(dateInst));
        int lastPageNum = es.countEventPages(totalEventQty);
        int pageInitialIndex = es.getPageInitialEventIndex(pageNum);
//        mav.addObject("totalEventQty", totalEventQty);
//        mav.addObject("lastPageNum", es.countEventPages(totalEventQty));
//        mav.addObject("pageInitialIndex", es.getPageInitialEventIndex(pageNum));
//        mav.addObject("currentDate", LocalDate.now());
//        mav.addObject("aWeekFromNow", LocalDate.now().plus(7, ChronoUnit.DAYS));

        return Response
        		.status(Status.OK)
        		.entity(EventCollectionDto.ofEvents(
        				events.stream().map(EventDto::ofEvent).collect(Collectors.toList()),
        				totalEventQty, lastPageNum, pageInitialIndex))
        		.build();
    }

    @GET
	@RequestMapping("/pitch/{pitchId}")
	public Response seePitch(
			@PathParam("pitchId") long id,
			@ModelAttribute("newEventForm") final EventForm form) throws PitchNotFoundException {

//		ModelAndView mav = new ModelAndView("pitch");
//
//		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
//		mav.addObject("scheduleHeaders", es.getScheduleDaysHeader());
//		mav.addObject("minHour", MIN_HOUR);
//		mav.addObject("maxHour", MAX_HOUR);
//		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
//		mav.addObject("schedule", es.convertEventListToBooleanSchedule(es.findCurrentEventsInPitch(id)));
//		mav.addObject("currentDate", LocalDate.now());
//		mav.addObject("currentDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
//		mav.addObject("aWeekFromNow", LocalDate.now().plus(7, ChronoUnit.DAYS));
//		mav.addObject("aWeekFromNowDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plus(7, ChronoUnit.DAYS));
//
//		return mav;
    	return null;
	}


    @POST
    @Path("/pitch/{pitchId}/event/create")
    public Response createEvent(
    		@PathParam("pitchId") long pitchId,
    		@Valid @ModelAttribute("newEventForm") final EventForm form,
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
    	return Response.ok().entity(EventDto.ofEvent(ev)).build();
    }


    private Response eventCreationError(String error, long pitchId, EventForm form)
    	throws PitchNotFoundException {
    	//ModelAndView mav = seePitch(pitchId, form);
		//mav.addObject(error, true);
		return null;//mav;
    }


    @DELETE
    @Path("/{id}")
	public Response deleteEvent(@PathParam("id") final long id)
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
		return null;//new ModelAndView("redirect:/events/1");
	}


    @POST
    @Path("/event/{eventId}/upvote")
    public Response upvote(@PathParam("eventId") final long eventid)
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(true, ev, loggedUser().getUserid());
    	}
    	return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/event/{eventId}/downvote")
    public Response downvote(@PathParam("eventId") final long eventid)
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(false, ev, loggedUser().getUserid());
    	}
    	return Response.status(Status.NO_CONTENT).build();
    }


//	@ExceptionHandler({ EventNotFoundException.class })
//	private ModelAndView eventNotFound() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ PitchNotFoundException.class })
//	private ModelAndView pitchNotFound() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ EventNotFinishedException.class })
//	private ModelAndView eventNotFinished() {
//		return new ModelAndView("404");
//	}
//
//
//	@ExceptionHandler({ UserNotFoundException.class })
//	private ModelAndView userNotFound() {
//		return new ModelAndView("404");
//	}

}
