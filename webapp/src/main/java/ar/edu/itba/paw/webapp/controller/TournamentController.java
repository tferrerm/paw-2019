package ar.edu.itba.paw.webapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.webapp.exception.TournamentEventNotFoundException;


@Controller
public class TournamentController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TournamentController.class);
	
	@Autowired
	private TournamentService ts;
	
	@Autowired
	private ClubService cs;

    @RequestMapping(value = "/tournament/{tournamentId}")
    public ModelAndView retrieveTournaments(@PathVariable long tournamentId) {
        ModelAndView mav = new ModelAndView("tournament");
        mav.addObject("tournament",  null);
        return mav;
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

    @RequestMapping(value = "tournament/{tournamentId}/team/{teamId}/join")
    public ModelAndView joinTeam(@PathVariable("tournamentId") long tournamentid, @PathVariable("teamId") long teamid) 
    		throws UserBusyException, UserAlreadyJoinedException {
    	
        ts.joinTeam(tournamentid, teamid, loggedUser().getUserid());
        
        return new ModelAndView("tournament");
    }

    @RequestMapping(value = "tournament-event/{id}")
    public ModelAndView retrieveTournamentEvent( @PathVariable long id) throws TournamentEventNotFoundException {
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
