package ar.edu.itba.paw.webapp.controller.admin;

import ar.edu.itba.paw.webapp.controller.BaseController;
import ar.edu.itba.paw.webapp.form.NewTournamentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;


@RequestMapping("/admin")
@Controller
public class AdminTournamentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminClubController.class);

    @RequestMapping("/tournament/new")
    public ModelAndView newTournament( @ModelAttribute("newTournamentForm") final NewTournamentForm form) {
        return new ModelAndView("admin/newTournament");
    }

    @RequestMapping("/tournament/create")
    public ModelAndView createTournament(
            @Valid @ModelAttribute("newTournamentForm") final NewTournamentForm form) {

//        LOGGER.debug("Tournament created {} with id {} created");

        return new ModelAndView("redirect:/admin/home");
    }


}
