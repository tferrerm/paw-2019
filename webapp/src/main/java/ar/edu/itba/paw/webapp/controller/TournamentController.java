package ar.edu.itba.paw.webapp.controller;

import java.time.Instant;
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

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.ClubService;
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
	
	@Autowired
	private ClubService cs;

    @RequestMapping(value = "/tournament/{tournamentId}")
    public ModelAndView retrieveTournament(@PathVariable("tournamentId") long tournamentid,
    		@RequestParam(value = "round", defaultValue = "1") final int roundPage) 
    		throws TournamentNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
		
		if(ts.inscriptionEnded(tournament)) {
			ModelAndView mav = new ModelAndView("tournament");
			mav.addObject("tournament",  tournament);
			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
			List<TournamentEvent> roundEvents = ts.findTournamentEventsByRound(tournamentid, roundPage);
			mav.addObject("roundEvents", roundEvents);
			Map<Long, Boolean> eventsHaveResult = new HashMap<>();
			for(TournamentEvent event : roundEvents) {
				eventsHaveResult.put(event.getEventid(), event.getFirstTeamScore() != null);
			}
			mav.addObject("eventsHaveResult", eventsHaveResult);
			mav.addObject("currRoundPage", roundPage);
			mav.addObject("maxRoundPage", tournament.getRounds());
			return mav;
		} else {
			ModelAndView mav = new ModelAndView("tournamentInscription");
			mav.addObject("tournament",  tournament);
		    //mav.addObject("teams",  ts.findTournamentTeams(tournamentid));
			List<TournamentTeam> teams = new ArrayList<>(tournament.getTeams());
			Comparator<TournamentTeam> cmp = new Comparator<TournamentTeam>() {
				@Override
				public int compare(TournamentTeam team1, TournamentTeam team2) {
					return ((Long)team1.getTeamid()).compareTo(team2.getTeamid());
				}
			};
			Collections.sort(teams, cmp);
		    mav.addObject("teams", teams);
		    Map<Long, List<User>> teamsUsers = ts.getTeamsUsers(tournamentid);
		    mav.addObject("teamsUsers", teamsUsers);
		    return mav;
		}
    }

    @RequestMapping(value = "/tournaments/{pageNum}")
    public ModelAndView retrieveTournaments(
			@PathVariable("pageNum") final int pageNum) {

        ModelAndView mav = new ModelAndView("tournamentList");
//
//        try {
	        List<Tournament> tournaments = ts.findBy(pageNum);

	        mav.addObject("tournaments", tournaments);
//	        mav.addObject("eventQty", events.size());
//
//	        Integer totalEventQty = es.countFilteredEvents(true, Optional.ofNullable(name),
//	        		Optional.ofNullable(establishment), Optional.ofNullable(sport), Optional.empty(),
//	        		Optional.ofNullable(vacancies), Optional.ofNullable(date));
//	        mav.addObject("totalEventQty", totalEventQty);
//        } catch(InvalidDateFormatException e) {
//        	mav.addObject("invalid_date_format", true);
//        	return mav;
//        } catch(InvalidVacancyNumberException e) {
//        	mav.addObject("invalid_number_format", true);
//        	return mav;
//        }
//
        
        return mav;
    }

    @RequestMapping(value = "tournament/{tournamentId}/team/{teamId}/join", method = { RequestMethod.POST })
    public ModelAndView joinTeam(@PathVariable("tournamentId") long tournamentid, @PathVariable("teamId") long teamid) 
    		throws UserBusyException, UserAlreadyJoinedException {
    	
        ts.joinTournament(tournamentid, teamid, loggedUser().getUserid());
        
        return new ModelAndView("redirect:/tournament/" + tournamentid);
    }
    
    @RequestMapping(value = "tournament/{tournamentId}/leave", method = { RequestMethod.POST })
    public ModelAndView leaveTournament(@PathVariable("tournamentId") long tournamentid) 
    		throws UserBusyException, UserAlreadyJoinedException {
    	
        ts.leaveTournament(tournamentid, loggedUser().getUserid());
        
        return new ModelAndView("redirect:/tournament/" + tournamentid);
    }

    @RequestMapping(value = "tournament-event/{id}")
    public ModelAndView retrieveTournamentEvent( @PathVariable long id) 
    		throws TournamentEventNotFoundException {
        ModelAndView mav = new ModelAndView("tournamentEvent");
//        TournamentEvent tEvent = ts.findTournamentEventById(id).orElseThrow(TournamentEventNotFoundException::new);
//        mav.addObject("tournamentEvent", tEvent );
        return mav;
    }

	@ExceptionHandler({ TournamentEventNotFoundException.class })
	private ModelAndView tournamentEventNotFound() {
		return new ModelAndView("404");
	}

}
