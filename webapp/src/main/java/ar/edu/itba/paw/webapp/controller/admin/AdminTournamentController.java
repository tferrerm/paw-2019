package ar.edu.itba.paw.webapp.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.TournamentNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.NewTournamentForm;

@RequestMapping("/admin")
@Controller
public class AdminTournamentController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTournamentController.class);
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;
	
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
	
	@RequestMapping(value = "/tournament/{tournamentId}")
    public ModelAndView retrieveTournaments(@PathVariable("tournamentId") long tournamentid) 
    		throws TournamentNotFoundException {
		
		Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
		
		if(ts.inscriptionEnded(tournament)) {
			ModelAndView mav = new ModelAndView("admin/tournament");
			mav.addObject("teamsScoresMap", ts.getTeamsScores(tournament));
			return mav;
		} else {
			ModelAndView mav = new ModelAndView("admin/tournamentInscription");
			mav.addObject("tournament",  tournament);
		    //mav.addObject("teams",  ts.findTournamentTeams(tournamentid));
		    mav.addObject("teams",  new ArrayList<>(tournament.getTeams()));
		    Map<Long, List<User>> teamsUsers = ts.getTeamsUsers(tournamentid);
		    mav.addObject("teamsUsers", teamsUsers);
		    return mav;
		}
    }
	
	
	@RequestMapping(value = "/tournaments/{pageNum}")
	public ModelAndView retrieveEvents(@PathVariable("pageNum") final int pageNum) {
		
		ModelAndView mav = new ModelAndView("admin/tournamentList");
		
		List<Tournament> tournaments = ts.findBy(pageNum);
		mav.addObject("tournaments", tournaments);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/club/{clubId}/tournament/new")
    public ModelAndView tournamentFormView(@PathVariable("clubId") long clubId,
    		@ModelAttribute("newTournamentForm") final NewTournamentForm form) 
    				throws ClubNotFoundException {
		
		ModelAndView mav = new ModelAndView("admin/newTournament");
		Club club = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
		mav.addObject("club", club);
		
		mav.addObject("availableHours", es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
		mav.addObject("minHour", MIN_HOUR);
		mav.addObject("maxHour", MAX_HOUR);
        
    	// MOSTRAR GRID DEL CLUB CON CANTIDAD DE PITCHES DISPONIBLE PARA TAL DEPORTE
    	
        return mav;
    }
    
	
    @RequestMapping(value = "/club/{clubId}/tournament/create", method = { RequestMethod.POST })
    public ModelAndView createTournament(@PathVariable("clubId") long clubId,
    		@Valid @ModelAttribute("newTournamentForm") final NewTournamentForm form,
			final BindingResult errors, HttpServletRequest request) throws ClubNotFoundException {
    	
    	if(errors.hasErrors()) {
    		return tournamentFormView(clubId, form);
    	}
    	
    	Club club = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
    	
    	/* Only SOCCER Tournaments are supported for now */
    	Tournament tournament = ts.create(form.getName(), Sport.SOCCER, club, form.getMaxTeams(), 
    			form.getTeamSize(), form.getFirstRoundDate(), form.getStartsAtHour(), 
    			form.getEndsAtHour(), form.getInscriptionEndDate(), loggedUser());
    	
    	return new ModelAndView("redirect:/admin/tournament/" + tournament.getTournamentid());
    }
    
    
    @RequestMapping(value = "/tournament/{tournamentId}/kick-user/{userId}", method = { RequestMethod.POST })
    public ModelAndView kickUserFromTournament(
    		@PathVariable("tournamentId") long tournamentid, @PathVariable("userId") long kickedUserId) 
    				throws UserNotFoundException, TournamentNotFoundException {
    	
    	Tournament tournament = ts.findById(tournamentid).orElseThrow(TournamentNotFoundException::new);
    	User kickedUser = us.findById(kickedUserId).orElseThrow(UserNotFoundException::new);
    	
    	ts.kickFromTournament(kickedUser, tournament);
    	ems.youWereKicked(kickedUser, tournament, LocaleContextHolder.getLocale());
    	
    	return new ModelAndView("redirect:/admin/tournament/" + tournamentid);
    }
    
    
    @ExceptionHandler({ TournamentNotFoundException.class })
	public ModelAndView tournamentNotFoundHandler() {
		return new ModelAndView("404");
	}
	
}
