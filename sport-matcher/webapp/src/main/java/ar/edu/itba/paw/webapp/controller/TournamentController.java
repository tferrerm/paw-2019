package ar.edu.itba.paw.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.exception.TeamAlreadyFilledException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.TournamentEvent;
import ar.edu.itba.paw.model.TournamentTeam;
import ar.edu.itba.paw.model.User;
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
	public Response haha() {
		return null;
	}
	
	/*@GET
    @Path("/{id}")
    public Response retrieveTournament(@PathParam("id") long tournamentid,
    		@RequestParam(value = "round", required = false) final Integer roundPage,
    		@RequestParam(value = "userBusyError", required = false) boolean userBusyError) 
    		throws TournamentNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	if(tournament.getInscriptionSuccess()) {
			if(roundPage == null) {
				return retrieveTournament(tournamentid, ts.getCurrentRound(tournament), false);
			}
			//ModelAndView mav = new ModelAndView("tournament");
//			mav.addObject("tournament",  tournament);
//			mav.addObject("club", tournament.getTournamentClub());
//			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
			List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPage);
//			mav.addObject("roundEvents", roundEvents);
			Map<Long, Boolean> eventsHaveResult = new HashMap<>();
			for(TournamentEvent event : roundEvents) {
				eventsHaveResult.put(event.getEventId(), event.getFirstTeamScore() != null);
			}
//			mav.addObject("eventsHaveResult", eventsHaveResult);
//			mav.addObject("currRoundPage", roundPage);
//			mav.addObject("maxRoundPage", tournament.getRounds());
			int currentRound = ts.getCurrentRound(tournament);
//			mav.addObject("currentRound", currentRound);
//			mav.addObject("hasFinished", ts.hasFinished(tournament.getRounds(), currentRound, roundEvents));
			
			return null;//mav;
			
		} else {
			//ModelAndView mav = new ModelAndView("tournamentInscription");
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
		   // mav.addObject("teams", teams);
		    Map<Long, List<User>> teamsUsers = ts.mapTeamMembers(tournamentid);
//		    mav.addObject("teamsUsers", teamsUsers);
//		    mav.addObject("roundsAmount", tournament.getRounds());
//		    mav.addObject("startsAt", ts.findTournamentEventsByRound(tournament.getTournamentid(), 1).get(0).getStartsAt());
//		    mav.addObject("userBusyError", userBusyError);
//		    mav.addObject("userJoined", loggedUser() != null ? ts.findUserTeam(tournamentid, loggedUser().getUserid()).isPresent() : false);
		    
		    return null;//mav;
		}
    }

    @GET
    @Path("/{pageNum}")
    public Response retrieveTournaments(
			@PathParam("pageNum") final int pageNum) {

        //ModelAndView mav = new ModelAndView("tournamentList");
        
	    List<Tournament> tournaments = ts.findBy(pageNum);
//	    mav.addObject("tournaments", tournaments);
//	    mav.addObject("tournamentQty", tournaments.size());
//		mav.addObject("page", pageNum);
//		mav.addObject("pageInitialIndex", ts.getPageInitialTournamentIndex(pageNum));
//		mav.addObject("totalTournamentQty", ts.countTournamentTotal());
//		mav.addObject("lastPageNum", ts.countTotalTournamentPages());
//		mav.addObject("now", Instant.now());
//        
        return null;//mav;
    }

    @POST
    @Path("/{id}/team/{teamId}/join")
    public Response joinTeam(@PathParam("id") long tournamentid, @PathParam("teamId") long teamid) 
    		throws UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.joinTournament(tournamentid, teamid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
			joinTournamentError("tournament_already_started", tournamentid);
		} catch (TeamAlreadyFilledException e) {
			joinTournamentError("team_already_filled", tournamentid);
		} catch (UserAlreadyJoinedException e) {
			joinTournamentError("already_joined_tournament", tournamentid);
		} catch(UserBusyException e) {
	    	return retrieveTournament(tournamentid, null, true);
		}
        
        LOGGER.debug("User {} joined Tournament {}", loggedUser(), tournamentid);
        
        return null;//new ModelAndView("redirect:/tournament/" + tournamentid);
    }
    
    
    private Response joinTournamentError(String error, long tournamentid) 
    		throws TournamentNotFoundException {
    	//ModelAndView mav = new ModelAndView("redirect:/tournament/" + tournamentid);
		//mav.addObject(error, true);
		return null;//mav;
    }
    
    @POST
    @Path("/{id}/leave")
    public Response leaveTournament(@PathParam("id") long tournamentid) 
    		throws UserBusyException, UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.leaveTournament(tournamentid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
			//ModelAndView mav = retrieveTournament(tournamentid, null, false);
			//mav.addObject("tournament_already_started", true);
			return null;//mav;
		}
        
        return null;//new ModelAndView("redirect:/tournament/" + tournamentid);
    }

    @GET
    @RequestMapping("/{id}/event/{eventId}")
    public Response retrieveTournamentEvent(@PathParam("id") long tournamentid,
    		@PathParam("eventId") long eventid) 
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
        //ModelAndView mav = new ModelAndView("tournamentEvent");
        Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
        //mav.addObject("tournament",  tournament);
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        //mav.addObject("tournamentEvent", tournamentEvent);
        //mav.addObject("firstTeamMembers", ts.findTeamMembers(tournamentEvent.getFirstTeam()));
        //mav.addObject("secondTeamMembers", ts.findTeamMembers(tournamentEvent.getSecondTeam()));
        return null;//mav;
    }

    
//	@ExceptionHandler({ TournamentEventNotFoundException.class })
//	private ModelAndView tournamentEventNotFound() {
//		return new ModelAndView("404");
//	}*/

}
