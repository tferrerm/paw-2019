package ar.edu.itba.paw.webapp.controller.admin;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.EventHasNotEndedException;
import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.dto.form.TournamentForm;
import ar.edu.itba.paw.webapp.dto.form.TournamentResultForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.TournamentNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("admin/clubs/{clubId}/tournaments")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminTournamentController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTournamentController.class);
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final int DAY_LIMIT = 7;
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private TournamentService ts;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private EventService es;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private EmailService ems;
	
	@Autowired
	private FormValidator validator;
	
	/*@GET
	@Path("/{id}")
    public Response retrieveTournament(@PathParam("id") long tournamentid,
    		@QueryParam("round") final Integer roundPage) 
    		throws TournamentNotFoundException {
		
		Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
		
		if(roundPage == null) {
			return retrieveTournament(tournamentid, ts.getMinRoundForResultInput(tournament), form);
		}
		
		if(tournament.getInscriptionSuccess()) {
			//ModelAndView mav = new ModelAndView("admin/tournament");
//			mav.addObject("tournament",  tournament);
//			mav.addObject("club", tournament.getTournamentClub());
//			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
			
			List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPage);
			// mav.addObject("roundEvents", roundEvents);
			Map<Long, Boolean> eventsHaveResult = new HashMap<>();
			for(TournamentEvent event : roundEvents) {
				eventsHaveResult.put(event.getEventId(), event.getFirstTeamScore() != null);
			}
//			mav.addObject("eventsHaveResult", eventsHaveResult);
//			mav.addObject("roundInPast", roundEvents.get(0).getEndsAt().compareTo(Instant.now()) <= 0);
//			
//			mav.addObject("currRoundPage", roundPage);
//			mav.addObject("maxRoundPage", tournament.getRounds());
			
			return null;//mav;
		} else {
			//ModelAndView mav = new ModelAndView("admin/tournamentInscription");
			//mav.addObject("tournament",  tournament);
			//mav.addObject("club", tournament.getTournamentClub());
			List<TournamentTeam> teams = new ArrayList<>(tournament.getTeams());
			Comparator<TournamentTeam> cmp = new Comparator<TournamentTeam>() {
				@Override
				public int compare(TournamentTeam team1, TournamentTeam team2) {
					return ((Long)team1.getTeamid()).compareTo(team2.getTeamid());
				}
			};
			Collections.sort(teams, cmp);
//		    mav.addObject("teams", teams);
//		    mav.addObject("roundsAmount", tournament.getRounds());
//		    mav.addObject("startsAt", ts.findTournamentEventsByRound(tournament.getTournamentid(), 1).get(0).getStartsAt());
		    Map<Long, List<User>> teamsUsers = ts.mapTeamMembers(tournamentid);
		    //mav.addObject("teamsUsers", teamsUsers);
		    return null;//mav;
		}
    }*/
	
	@POST
	@Path("/{id}/events/{eventId}/result")
    public Response setTournamentEventResult(@PathParam("clubId") long clubid,
    		@PathParam("id") long tournamentid, @PathParam("eventId") long eventid,
    		@FormDataParam("firstResult") String first, @FormDataParam("secondResult") String second)
    				throws TournamentNotFoundException, FormValidationException, EventHasNotEndedException {

		Integer firstResult = tryInteger(first);
    	Integer secondResult = tryInteger(second);
    	
    	validator.validate(new TournamentResultForm()
    			.withFirstResult(firstResult)
    			.withSecondResult(secondResult));
		
		Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);

		ts.postTournamentEventResult(tournament, eventid, firstResult, secondResult);
	    return Response.status(Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("/{pageNum}")
	public Response retrieveEvents(@PathParam("clubId") long clubid, @PathParam("pageNum") final int pageNum) {
		
		//ModelAndView mav = new ModelAndView("admin/tournamentList");
		
		List<Tournament> tournaments = ts.findBy(pageNum);
//		mav.addObject("tournaments", tournaments);
//		mav.addObject("tournamentQty", tournaments.size());
//		mav.addObject("page", pageNum);
//		mav.addObject("pageInitialIndex", ts.getPageInitialTournamentIndex(pageNum));
//		mav.addObject("totalTournamentQty", ts.countTournamentTotal());
//		mav.addObject("lastPageNum", ts.countTotalTournamentPages());
//		mav.addObject("now", Instant.now());
		
		return null;//mav;
	}
	
	@GET
	@Path("/tournaments/new")
    public Response tournamentFormView(@PathParam("clubId") long clubid,
    		@FormDataParam("newTournamentForm") final TournamentForm form) 
    				throws ClubNotFoundException {
		
		//ModelAndView mav = new ModelAndView("admin/newTournament");
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
//		mav.addObject("club", club);
//		
//		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
//		mav.addObject("minHour", MIN_HOUR);
//		mav.addObject("maxHour", MAX_HOUR);
		
		List<Event> clubEvents = cs.findCurrentEventsInClub(clubid, Sport.SOCCER);
//		mav.addObject("schedule", cs.convertEventListToSchedule(clubEvents, MIN_HOUR, MAX_HOUR, DAY_LIMIT));
//		mav.addObject("scheduleHeaders", es.getScheduleDaysHeader());
//		mav.addObject("pitchQty", club.getClubPitches().stream()
//				.filter(p -> p.getSport() == Sport.SOCCER).collect(Collectors.toList()).size());
//		mav.addObject("currentDate", LocalDate.now());
//		mav.addObject("currentDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
//		mav.addObject("aWeekFromNowDate", LocalDate.now().plus(7, ChronoUnit.DAYS));
//		mav.addObject("aWeekFromNowDateTime", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plus(7, ChronoUnit.DAYS));
    	
        return null;//mav;
    }
    
	@POST
    public Response createTournament(@PathParam("clubId") long clubId,
    		@FormDataParam("name") String name, @FormDataParam("maxTeams") String maxTeams,
    		@FormDataParam("teamSize") String teamSize, @FormDataParam("firstRoundDate") String firstRoundDate,
    		@FormDataParam("startsAtHour") String startsAtHour, @FormDataParam("endsAtHour") String endsAtHour,
    		@FormDataParam("inscriptionEndDate") String inscriptionEndDate)
    				throws ClubNotFoundException, FormValidationException {
    	
    	Integer mt = tryInteger(maxTeams);
    	Integer ts = tryInteger(teamSize);
    	Instant frd = tryInstantStartOfDay(firstRoundDate, TIME_ZONE);
    	Integer sa = tryInteger(startsAtHour);
    	Integer ea = tryInteger(endsAtHour);
    	Instant ied = tryDateTimeToInstant(inscriptionEndDate, TIME_ZONE);
    	
    	validator.validate(new TournamentForm()
    			.withName(name)
    			.withMaxTeams(mt)
    			.withTeamSize(ts)
    			.withtFirstRoundDate(frd)
    			.withStartsAtHour(sa)
    			.withEndsAtHour(ea)
    			.withInscriptionEndDate(ied));
    	
    	Club club = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
    	Tournament tournament = null;
    	
//    	try {
//    		// Only SOCCER Tournaments are supported for now
//        	tournament = ts.create(form.getName(), Sport.SOCCER, club, maxTeams,
//        			teamSize, firstRoundDate, startsAt, endsAt, inscriptionEndDate, loggedUser());
//    	} catch(DateInPastException e) {
//    		return tournamentCreationError("event_in_past", clubId, form);
//    	} catch(MaximumDateExceededException e) {
//    		return tournamentCreationError("date_exceeded", clubId, form);
//    	} catch(EndsBeforeStartsException e) {
//    		return tournamentCreationError("ends_before_starts", clubId, form);
//    	} catch(HourOutOfRangeException e) {
//    		return tournamentCreationError("hour_out_of_range", clubId, form);
//    	} catch(InvalidTeamAmountException e) {
//    		return tournamentCreationError("invalid_team_amount", clubId, form);
//    	} catch(UnevenTeamAmountException e) {
//    		return tournamentCreationError("uneven_team_amount", clubId, form);
//    	} catch(InvalidTeamSizeException e) {
//    		return tournamentCreationError("invalid_team_size", clubId, form);
//    	} catch(InscriptionDateInPastException e) {
//    		return tournamentCreationError("inscription_date_in_past", clubId, form);
//    	} catch(InscriptionDateExceededException e) {
//    		return tournamentCreationError("inscription_date_exceeded", clubId, form);
//    	} catch(InsufficientPitchesException e) {
//    		return tournamentCreationError("insufficient_pitches", clubId, form);
//    	}
    	
    	LOGGER.debug("Tournament {} created", tournament);
    	
    	return null;
    }
    
    @DELETE
    @Path("/{id}")
	public Response deleteEvent(@PathParam("id") final long tournamentid)
			throws TournamentNotFoundException, InscriptionDateInPastException {
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	Map<Long, List<User>> teamsMap = ts.mapTeamMembers(tournamentid);
    	
    	ts.deleteTournament(tournamentid);
		
    	for(List<User> teamMembers : teamsMap.values()) {
			for(User user : teamMembers) {
				ems.tournamentCancelled(user, tournament, LocaleContextHolder.getLocale());
			}
		}
    	
		LOGGER.debug("Deleted tournament with id {}", tournamentid);
		return Response.status(Status.NO_CONTENT).build();
	}
    
    @POST
    @Path("/{id}/kick-user/{userId}")
    public Response kickUserFromTournament(
    		@PathParam("id") long tournamentid, @PathParam("userId") long kickedUserId) 
    				throws UserNotFoundException, TournamentNotFoundException, InscriptionDateInPastException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	User kickedUser = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
    	
    	ts.kickFromTournament(kickedUser, tournament);
    	ems.youWereKicked(kickedUser, tournament, LocaleContextHolder.getLocale());
    	
    	return Response.status(Status.NO_CONTENT).build();
    }
    
    
//    @ExceptionHandler({ TournamentNotFoundException.class })
//	public ModelAndView tournamentNotFoundHandler() {
//		return new ModelAndView("404");
//	}
	
}
