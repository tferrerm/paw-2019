package ar.edu.itba.paw.webapp.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.TournamentService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.NewTournamentForm;

@RequestMapping("/admin")
@Controller
public class AdminTournamentController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminTournamentController.class);
	
	@Autowired
	private TournamentService ts;
	
	@Autowired
	private ClubService cs;
	
	@RequestMapping(value = "/club/{clubId}/tournament/new")
    public ModelAndView tournamentFormView(@PathVariable("clubId") long tournamentId,
    		@ModelAttribute("newTournamentForm") final NewTournamentForm form) {
        
    	// MOSTRAR GRID DEL CLUB CON CANTIDAD DE PITCHES DISPONIBLE PARA TAL DEPORTE
    	
        return new ModelAndView();
    }
    
    @RequestMapping(value = "/club/{clubId}/tournament/create")
    public ModelAndView createTournament(@PathVariable("clubId") long clubId,
    		@Valid @ModelAttribute("newTournamentForm") final NewTournamentForm form,
			final BindingResult errors, HttpServletRequest request) throws ClubNotFoundException {
    	
    	Sport sport = null;
		try {
			sport = Sport.valueOf(form.getSport());
		} catch(IllegalArgumentException e) {
			LOGGER.warn("Unable to convert sport to enum");
			errors.rejectValue("sport", "sport_not_in_list");
		}
    	
    	if(errors.hasErrors()) {
    		return tournamentFormView(clubId, form);
    	}
    	
    	Club club = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
    	
    	Tournament tournament = ts.create(form.getName(), sport, club, form.getMaxTeams(), 
    			form.getTeamSize(), form.getFirstRoundDate(), form.getStartsAtHour(), 
    			form.getEndsAtHour(), form.getInscriptionEndDate(), loggedUser());
    	
    	return new ModelAndView("redirect:/tournament/" + tournament.getTournamentId());
    }
	
}
