package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.exception.TeamAlreadyFilledException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.FullTournamentDto;
import ar.edu.itba.paw.webapp.dto.TournamentCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentDto;
import ar.edu.itba.paw.webapp.dto.TournamentEventCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentEventDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamInscriptionsCollectionDto;
import ar.edu.itba.paw.webapp.dto.TournamentTeamInscriptionsDto;
import ar.edu.itba.paw.webapp.dto.UserCollectionDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.TournamentEventNotFoundException;
import ar.edu.itba.paw.webapp.exception.TournamentNotFoundException;

@Path("tournaments")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private TournamentService ts;
	
	@GET
    @Path("/{id}")
    public Response retrieveTournament(@PathParam("id") long tournamentid//,
    		/*@QueryParam("round") final Integer roundPage*/)
    		throws TournamentNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	Instant startsAt = ts.findTournamentEventsByRound(tournament.getTournamentid(), 1).get(0).getStartsAt();
    	
    	return Response.ok(FullTournamentDto.ofTournament(tournament, tournament.getRounds(), startsAt)).build();
    	
//    	if(tournament.getInscriptionSuccess()) {
//			if(roundPage == null) {
//				return Response.status(Status.NOT_FOUND).build();
//			}
////			mav.addObject("club", tournament.getTournamentClub());
////			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
//			List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPage);
////			mav.addObject("roundEvents", roundEvents);
//			Map<Long, Boolean> eventsHaveResult = new HashMap<>();
//			for(TournamentEvent event : roundEvents) {
//				eventsHaveResult.put(event.getEventId(), event.getFirstTeamScore() != null);
//			}
////			mav.addObject("eventsHaveResult", eventsHaveResult);
////			mav.addObject("currRoundPage", roundPage);
////			mav.addObject("maxRoundPage", tournament.getRounds());
//			int currentRound = ts.getCurrentRound(tournament);
////			mav.addObject("currentRound", currentRound);
////			mav.addObject("hasFinished", ts.hasFinished(tournament.getRounds(), currentRound, roundEvents));
//			
//			return null;//mav;

    }

    @GET
    public Response retrieveTournaments(@QueryParam("pageNum") final int pageNum) {

        List<Tournament> tournaments = ts.findBy(pageNum);
	    
	    int totalTournamentQty = ts.countTournamentTotal();
	    int lastPageNum = ts.countTotalTournamentPages();
        int pageInitialIndex = ts.getPageInitialTournamentIndex(pageNum);

        return Response
        		.status(Status.OK)
        		.entity(TournamentCollectionDto.ofTournaments(
        				tournaments.stream().map(t -> TournamentDto.ofTournament(t)).collect(Collectors.toList()),
        				totalTournamentQty, lastPageNum, pageInitialIndex))
        		.build();
    }
    
    @GET
    @Path("/{id}/inscription")
    public Response retrieveTournamentInscription(@PathParam("id") long tournamentid)
    		throws TournamentNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	List<TournamentTeam> teams = tournament.getTeams().stream().map(t ->
    			ts.findByTeamId(t.getTeamid()).get()).collect(Collectors.toList());
    	
    	boolean hasJoined = loggedUser() != null ? 
    			ts.findUserTeam(tournamentid, loggedUser().getUserid()).isPresent() : false;
    	
    	return Response.ok(TournamentTeamInscriptionsCollectionDto.ofTeams(
    			teams.stream().map(t ->
					TournamentTeamInscriptionsDto.ofTeam(
						t,
						ts.getTeamMembers(t).stream().map(UserDto::ofUser).collect(Collectors.toList())
					)
    	    	).collect(Collectors.toList()), hasJoined)
    		).build();
    }

    @POST
    @Path("/{id}/team/{teamId}/join")
    public Response joinTeam(@PathParam("id") long tournamentid, @PathParam("teamId") long teamid) 
    		throws UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.joinTournament(tournamentid, teamid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
		} catch (TeamAlreadyFilledException e) {
		} catch (UserAlreadyJoinedException e) {
		} catch(UserBusyException e) {
		}
        
        LOGGER.debug("User {} joined Tournament {}", loggedUser(), tournamentid);
        
        return Response.status(Status.NO_CONTENT).build();//new ModelAndView("redirect:/tournament/" + tournamentid);
    }
    
    @POST
    @Path("/{id}/leave")
    public Response leaveTournament(@PathParam("id") long tournamentid) 
    		throws UserBusyException, UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.leaveTournament(tournamentid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
		}
        
        return Response.status(Status.NO_CONTENT).build();//new ModelAndView("redirect:/tournament/" + tournamentid);
    }

    @GET
    @Path("/{id}/events/{eventId}")
    public Response retrieveTournamentEvent(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) 
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
        Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        //mav.addObject("firstTeamMembers", ts.findTeamMembers(tournamentEvent.getFirstTeam()));
        //mav.addObject("secondTeamMembers", ts.findTeamMembers(tournamentEvent.getSecondTeam()));
        return Response.ok(TournamentEventDto.ofTournamentEvent(
        		tournamentEvent/*, false*/)).build(); // VER SI ES PARTICIPANTE
    }
    
    @GET
    @Path("/{id}/events/{eventId}/first-team-members")
    public Response retrieveFirstTeamMembers(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) throws TournamentNotFoundException, TournamentEventNotFoundException {
    	return retrieveTeamMembers(tournamentid, eventid, 1);
    }
    
    @GET
    @Path("/{id}/events/{eventId}/second-team-members")
    public Response retrieveSecondTeamMembers(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) throws TournamentNotFoundException, TournamentEventNotFoundException {
    	return retrieveTeamMembers(tournamentid, eventid, 2);
    }
    
    private Response retrieveTeamMembers(long tournamentid, long eventid, int teamNumber)
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        List<User> teamMembers = teamNumber == 1 ? 
        		ts.findTeamMembers(tournamentEvent.getFirstTeam()) : 
        			ts.findTeamMembers(tournamentEvent.getSecondTeam());
        
        return Response.ok(UserCollectionDto.ofUsers(teamMembers.stream()
        		.map(UserDto::ofUser).collect(Collectors.toList()))).build();
    }
    
    @GET
    @Path("/{id}/teams")
    public Response retrieveTournamentTeams(@PathParam("id") long tournamentid)
    		throws TournamentNotFoundException {

    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	return Response.ok(TournamentTeamCollectionDto.ofTeams(
    			tournament.getTeams().stream().map(TournamentTeamDto::ofTeam).collect(Collectors.toList()))
    		).build();
    }
    
    @GET
    @Path("/{id}/round")
    public Response retrieveTournamentRound(@PathParam("id") long tournamentid,
    	@QueryParam("roundPageNum") final Integer roundPage) throws TournamentNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	int currentRound = ts.getCurrentRound(tournament);
    	Integer roundPageNum = roundPage;
    	if(roundPageNum == null)
    		roundPageNum = currentRound;
    	
        List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPageNum);
    	
    	return Response.ok(TournamentEventCollectionDto.ofEvents(roundEvents.stream()
    			.map(TournamentEventDto::ofTournamentEvent).collect(Collectors.toList()), roundPageNum, currentRound)
    		).build();
    }

//	@ExceptionHandler({ TournamentEventNotFoundException.class })
//	private ModelAndView tournamentEventNotFound() {
//		return new ModelAndView("404");
//	}

}
