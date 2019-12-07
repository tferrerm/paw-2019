package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import ar.edu.itba.paw.exception.DateInPastException;
import ar.edu.itba.paw.exception.EntityNotFoundException;
import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.EventNotFinishedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Inscription;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.FullEventDto;
import ar.edu.itba.paw.webapp.dto.InscriptionCollectionDto;
import ar.edu.itba.paw.webapp.dto.InscriptionDto;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.EventNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("events")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class EventController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@Context
	private	UriInfo	uriInfo;

	@Autowired
	private EventService es;

    @Autowired
    private EmailService ems;

    @Autowired
    private UserService us;

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

    @GET
    @Path("/{id}")
    public Response retrieveElement(@PathParam("id") long id)
    		throws EventNotFoundException, ClubNotFoundException {

    //	Optional<TournamentEvent> tournamentEvent = ts.findTournamentEventById(id);
    //	if(tournamentEvent.isPresent()) {
    //		return null;//new ModelAndView("redirect:/tournament/" + tournamentEvent.get().getTournament().getTournamentid() + "/event/" + id);
    //	}

    	Event event = es.findByEventId(id).orElseThrow(EventNotFoundException::new);
    	List<Inscription> inscriptions = event.getInscriptions();
	    User current = loggedUser();

        boolean isParticipant = false;
        if (loggedUser() != null) {
        	Long count = inscriptions.stream()
        		.filter(x -> x.getInscriptedUser().equals(current))
        		.collect(Collectors.counting());
        	isParticipant = count > 0;
        }
//        mav.addObject("has_ended", Instant.now().isAfter(event.getEndsAt()));
        int voteBalance = es.getVoteBalance(event.getEventId());
        int userVote = 0;
        if (loggedUser() != null) {
        	userVote = es.getUserVote(event.getEventId(), current.getUserid());
        }
        
        return Response.ok(FullEventDto.ofEvent(
        		event, isParticipant, voteBalance, userVote)
        		).build();
    }

    @POST
    @Path("/{id}/join")
    public Response joinEvent(@PathParam("id") long id)
    	throws EntityNotFoundException, DateInPastException, EventFullException, UserBusyException {
	    /* HARDCODEADO HARDCODED InscriptionClosedException */
	    
	    if (loggedUser() != null) {
		    es.joinEvent(loggedUser().getUserid(), id);
	    }
        return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/{id}/leave")
    public Response leaveEvent(@PathParam("id") long id) throws DateInPastException, EntityNotFoundException {
    	if (loggedUser() != null) {
    		es.leaveEvent(id, loggedUser().getUserid());
    	}
        return Response.status(Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/kick-user/{userId}")
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
    		Response.status(Status.BAD_REQUEST).build();
    	if(dateInst == null && date != null && !date.isEmpty())
    		Response.status(Status.BAD_REQUEST).build();

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
        				events.stream().map(e -> EventDto.ofEvent(e, true)).collect(Collectors.toList()),
        				totalEventQty, lastPageNum, pageInitialIndex))
        		.build();
    }

//    @GET
//	@RequestMapping("/pitch/{pitchId}")
//	public Response seePitch(
//			@PathParam("pitchId") long id,
//			@ModelAttribute("newEventForm") final EventForm form) throws PitchNotFoundException {
//
////		ModelAndView mav = new ModelAndView("pitch");
////
////		mav.addObject("pitch", ps.findById(id).orElseThrow(PitchNotFoundException::new));
////		mav.addObject("scheduleHeaders", es.getScheduleDaysHeader());
////		mav.addObject("minHour", MIN_HOUR);
////		mav.addObject("maxHour", MAX_HOUR);
////		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
////		mav.addObject("schedule", es.convertEventListToBooleanSchedule(es.findCurrentEventsInPitch(id))); OTRO ENDPOINT
////		mav.addObject("currentDate", LocalDate.now());
////		mav.addObject("currentDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
////		mav.addObject("aWeekFromNow", LocalDate.now().plus(7, ChronoUnit.DAYS));
////		mav.addObject("aWeekFromNowDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plus(7, ChronoUnit.DAYS));
////
////		return mav;
//    	return null;
//	}

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
				return Response.status(Status.NO_CONTENT).build();
    	}
		return Response.status(Status.FORBIDDEN).build();
	}


    @POST
    @Path("/{eventId}/upvote")
    public Response upvote(@PathParam("eventId") final long eventid)
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(true, ev, loggedUser().getUserid());
    	}
    	return Response.status(Status.NO_CONTENT).build();
    }


    @POST
    @Path("/{eventId}/downvote")
    public Response downvote(@PathParam("eventId") final long eventid)
    	throws EventNotFoundException, UserNotAuthorizedException, EventNotFinishedException {
    	if (loggedUser() != null) {
	    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
	    	es.vote(false, ev, loggedUser().getUserid());
    	}
    	return Response.status(Status.NO_CONTENT).build();
    }
    
    
    @GET
    @Path("/{id}/inscriptions")
    public Response getInscriptions(@PathParam("id") final long eventid) throws EventNotFoundException {
    	Event ev = es.findByEventId(eventid).orElseThrow(EventNotFoundException::new);
    	return Response.ok(InscriptionCollectionDto.ofInscriptions(ev.getInscriptions().stream()
    			.map(InscriptionDto::ofInscription).collect(Collectors.toList()))).build();
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
