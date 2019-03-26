package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.FiltersForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController extends BaseController {
	
	@RequestMapping("/home")
	public ModelAndView home()	{
		return new ModelAndView("home");
	}

	@RequestMapping("/list")
	public ModelAndView list(@ModelAttribute("filtersForm") final FiltersForm form)	{
		return new ModelAndView("list");
	}

    @RequestMapping(value = "/event/{id}")
    public ModelAndView retrieveElement(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("event");
        /*Query*/
        return mav;
    }

    @RequestMapping(value = "/events")
    public ModelAndView retrieveElement( @ModelAttribute("filtersForm") final FiltersForm form) {
        ModelAndView mav = new ModelAndView("list");
        /*Query*/
        return mav;
    }





}
