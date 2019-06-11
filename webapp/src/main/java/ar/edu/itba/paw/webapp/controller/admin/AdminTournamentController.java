package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.form.NewPitchForm;
import ar.edu.itba.paw.webapp.form.NewTournamentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminTournamentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminClubController.class);

    @RequestMapping("/tournament/new")
    public ModelAndView newTournament( @ModelAttribute("newTournamentForm") final NewTournamentForm form) {
        return new ModelAndView("admin/newTournament");
    }

    @RequestMapping("/tournament/create")
    public ModelAndView createTournament( @Valid @ModelAttribute("newTournamentForm") final NewTournamentForm form) {

        ModelAndView mav = new ModelAndView("admin/newTournament");
        mav.addObject("sports", Sport.values());
        return mav;
    }


}
