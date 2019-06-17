package ar.edu.itba.paw.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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


@Controller
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
	
	@Autowired
	private TournamentService ts;

	
    @RequestMapping(value = "/tournament/{tournamentId}")
    public ModelAndView retrieveTournament(@PathVariable("tournamentId") long tournamentid,
    		@RequestParam(value = "round", required = false) final Integer roundPage) 
    		throws TournamentNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	
    	if(roundPage == null) {
			return retrieveTournament(tournamentid, ts.getCurrentRound(tournament));
		}
		
		if(tournament.getInscriptionSuccess()) {
			ModelAndView mav = new ModelAndView("tournament");
			mav.addObject("tournament",  tournament);
			mav.addObject("club", tournament.getTournamentClub());
			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
			List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPage);
			mav.addObject("roundEvents", roundEvents);
			Map<Long, Boolean> eventsHaveResult = new HashMap<>();
			for(TournamentEvent event : roundEvents) {
				eventsHaveResult.put(event.getEventId(), event.getFirstTeamScore() != null);
			}
			mav.addObject("eventsHaveResult", eventsHaveResult);
			mav.addObject("currRoundPage", roundPage);
			mav.addObject("maxRoundPage", tournament.getRounds());
			return mav;
		} else {
			ModelAndView mav = new ModelAndView("tournamentInscription");
			mav.addObject("tournament",  tournament);
			mav.addObject("club", tournament.getTournamentClub());
		    //mav.addObject("teams",  ts.findTournamentTeams(tournamentid));
			List<TournamentTeam> teams = new ArrayList<>(tournament.getTeams());
			Comparator<TournamentTeam> cmp = new Comparator<TournamentTeam>() {
				@Override
				public int compare(TournamentTeam team1, TournamentTeam team2) {
					return ((Long)team1.getTeamid()).compareTo(team2.getTeamid()); // VER DE SACAR
				}
			};
			Collections.sort(teams, cmp);
		    mav.addObject("teams", teams);
		    Map<Long, List<User>> teamsUsers = ts.mapTeamMembers(tournamentid);
		    mav.addObject("teamsUsers", teamsUsers);
		    mav.addObject("roundsAmount", tournament.getRounds());
		    mav.addObject("startsAt", ts.findTournamentEventsByRound(tournament.getTournamentid(), 1).get(0).getStartsAt());
		    mav.addObject("userJoined", ts.findUserTeam(tournamentid, loggedUser().getUserid()).isPresent());
		    return mav;
		}
    }

    
    @RequestMapping(value = "/tournaments/{pageNum}")
    public ModelAndView retrieveTournaments(
			@PathVariable("pageNum") final int pageNum) {

        ModelAndView mav = new ModelAndView("tournamentList");
        
	    List<Tournament> tournaments = ts.findBy(pageNum);
	    mav.addObject("tournaments", tournaments);
	    mav.addObject("tournamentQty", tournaments.size());
		mav.addObject("page", pageNum);
		mav.addObject("pageInitialIndex", ts.getPageInitialTournamentIndex(pageNum));
		mav.addObject("totalTournamentQty", ts.countTournamentTotal());
		mav.addObject("lastPageNum", ts.countTotalTournamentPages());
        
        return mav;
    }

    
    @RequestMapping(value = "/tournament/{tournamentId}/team/{teamId}/join", method = { RequestMethod.POST })
    public ModelAndView joinTeam(@PathVariable("tournamentId") long tournamentid, @PathVariable("teamId") long teamid) 
    		throws UserBusyException, UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.joinTournament(tournamentid, teamid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
			joinTournamentError("tournament_already_started", tournamentid);
		} catch (TeamAlreadyFilledException e) {
			joinTournamentError("team_already_filled", tournamentid);
		} catch (UserAlreadyJoinedException e) {
			joinTournamentError("already_joined_tournament", tournamentid);
		}
        
        LOGGER.debug("User {} joined Tournament {}", loggedUser(), tournamentid);
        
        return new ModelAndView("redirect:/tournament/" + tournamentid);
    }
    
    
    private ModelAndView joinTournamentError(String error, long tournamentid) 
    		throws TournamentNotFoundException {
    	ModelAndView mav = retrieveTournament(tournamentid, null);
		mav.addObject(error, true);
		return mav;
    }
    
    
    @RequestMapping(value = "/tournament/{tournamentId}/leave", method = { RequestMethod.POST })
    public ModelAndView leaveTournament(@PathVariable("tournamentId") long tournamentid) 
    		throws UserBusyException, UserAlreadyJoinedException, TournamentNotFoundException {
    	
        try {
			ts.leaveTournament(tournamentid, loggedUser().getUserid());
		} catch (InscriptionDateInPastException e) {
			ModelAndView mav = retrieveTournament(tournamentid, null);
			mav.addObject("tournament_already_started", true);
			return mav;
		}
        
        return new ModelAndView("redirect:/tournament/" + tournamentid);
    }

    
    @RequestMapping(value = "/tournament/{tournamentId}/event/{eventId}")
    public ModelAndView retrieveTournamentEvent(@PathVariable("tournamentId") long tournamentid,
    		@PathVariable("eventId") long eventid) 
    		throws TournamentNotFoundException, TournamentEventNotFoundException {
        ModelAndView mav = new ModelAndView("tournamentEvent");
        Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
        mav.addObject("tournament",  tournament);
        TournamentEvent tournamentEvent = ts.findTournamentEventById(eventid).orElseThrow(TournamentEventNotFoundException::new);
        mav.addObject("tournamentEvent", tournamentEvent);
        mav.addObject("firstTeamMembers", ts.findTeamMembers(tournamentEvent.getFirstTeam()));
        mav.addObject("secondTeamMembers", ts.findTeamMembers(tournamentEvent.getSecondTeam()));
        return mav;
    }

    
	@ExceptionHandler({ TournamentEventNotFoundException.class })
	private ModelAndView tournamentEventNotFound() {
		return new ModelAndView("404");
	}

}
