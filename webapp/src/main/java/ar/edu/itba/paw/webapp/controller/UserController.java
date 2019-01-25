package ar.edu.itba.paw.webapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.NewUserForm;

@Controller
public class UserController {
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@RequestMapping("/user/{userId}")
	public ModelAndView userProfile(@PathVariable("userId") long userid) 
	throws UserNotFoundException {
		final ModelAndView mav = new ModelAndView("profile");
		mav.addObject("user", us.findById(userid)
				.orElseThrow(UserNotFoundException::new));
		return mav;
	}
	
	@RequestMapping("/")
	public ModelAndView index(@ModelAttribute("signupForm") final NewUserForm form) {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/user/create", method = { RequestMethod.POST })
	public ModelAndView create(
			@Valid @ModelAttribute("signupForm") final NewUserForm form,
			final BindingResult errors
	) {
		if(errors.hasErrors()) {
			return index(form);
		}
		final User u = us.create(form.getUsername(), form.getPassword());
		return new ModelAndView("redirect:/user/" + u.getUserid());
	}
}
