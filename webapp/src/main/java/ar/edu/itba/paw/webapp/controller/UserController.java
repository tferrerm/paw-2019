package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.NewUserForm;
import exception.UserAlreadyExistsException;

@Controller
public class UserController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping(value = "/login", method = {RequestMethod.GET})
	public ModelAndView login(@RequestParam(name = "error", defaultValue = "false") boolean error) {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("error", error);
		return mav;
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
		if(!form.repeatPasswordMatching())
		 	errors.rejectValue("repeatPassword", "different_passwords");
		if(errors.hasErrors()) {
			return index(form);
		}
		User u;
		final MultipartFile profilePicture = form.getProfilePicture();
		final String encodedPassword = passwordEncoder.encode(form.getPassword());
		try {
			byte[] picture = profilePicture.getBytes();
			u = us.create(form.getUsername(), encodedPassword, Role.ROLE_USER, picture);
		} catch(IllegalArgumentException | IOException e) {
			LOGGER.error("Bad profile picture {}", profilePicture.getOriginalFilename());
			ModelAndView mav = index(form);
			mav.addObject("fileErrorMessage", profilePicture.getOriginalFilename());
			return mav;
		} catch(UserAlreadyExistsException e) {
			LOGGER.error("User tried to register with repeated email {}", form.getUsername());
			ModelAndView mav = index(form);
			mav.addObject("emailError", form.getUsername());
			return mav;
		}
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				u.getUsername(), u.getPassword());
		authToken.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ModelAndView("redirect:/user/" + u.getUserid());
	}
}
