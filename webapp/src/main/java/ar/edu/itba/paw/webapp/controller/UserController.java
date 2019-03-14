package ar.edu.itba.paw.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.NewUserForm;

@Controller
public class UserController extends BaseController {
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@RequestMapping("/login")
	public ModelAndView login()	{
		return new ModelAndView("login");
	}
	
    @RequestMapping(value = "/logout" , method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        request.getSession().invalidate();
        return new ModelAndView("login");
    }
	
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
			final BindingResult errors,
			HttpServletRequest request
	) {
		if(errors.hasErrors()) {
			return index(form);
		}
		String encodedPassword = passwordEncoder.encode(form.getPassword());
		final User u = us.create(form.getUsername(), encodedPassword, Role.ROLE_USER);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				u.getUsername(), u.getPassword());
		authToken.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ModelAndView("redirect:/home");
	}
}
