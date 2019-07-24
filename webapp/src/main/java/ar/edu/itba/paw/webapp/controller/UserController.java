package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;
import ar.edu.itba.paw.webapp.auth.CustomPermissionsHandler;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.NewUserForm;

@Path("users")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class UserController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final String DEFAULT_PROFILE_PICTURE = "profile_default.png";
	
	@Context
	private	UriInfo	uriInfo;
	
	@Qualifier("userServiceImpl")
	@Autowired
	private UserService us;
	
	@Autowired
	private ProfilePictureService pps;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Qualifier("eventServiceImpl")
	@Autowired
	private EventService es;
	
	@Autowired
	private EmailService ems;
	
	@Autowired
	private CustomPermissionsHandler cph;

	@RequestMapping(value = "/login", method = {RequestMethod.GET})
	public Object login(@RequestParam(name = "error", defaultValue = "false") boolean error) {
		if(cph.isAuthenticated()) {
			if(cph.isAdmin())
				return null;//return new ModelAndView("redirect:/admin/");
			//return new ModelAndView("redirect:/home");
		}
		//ModelAndView mav = new ModelAndView("login");
		//mav.addObject("error", error);
		return null;//mav;
	}
	
    @RequestMapping(value = "/logout" , method = RequestMethod.GET)
    public Object logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        request.getSession().invalidate();
//        return new ModelAndView("login");
        return null;
    }
	
    @GET
    @Path("/{id}")
	public Response userProfile(@PathParam("userId") long userid,
			@RequestParam(value = "cmt", defaultValue = "1") final int pageNum,
			@ModelAttribute("commentForm") final CommentForm form) 
					throws UserNotFoundException {
		
		//final ModelAndView mav = new ModelAndView("profile");
		
//		mav.addObject("user", us.findById(userid).orElseThrow(UserNotFoundException::new));
//		mav.addObject("currEventsParticipant", es.countByUserInscriptions(true, userid));
//		mav.addObject("currEventsOwned", es.countByOwner(true, userid));
//		mav.addObject("pastEventsParticipant", es.countByUserInscriptions(false, userid));
//		mav.addObject("favoriteSport", es.getFavoriteSport(userid).orElse(null));
//		mav.addObject("mainClub", es.getFavoriteClub(userid).orElse(null));
//		mav.addObject("votes_received", us.countVotesReceived(userid));
//		
//		mav.addObject("haveRelationship", loggedUser() != null ? us.haveRelationship(loggedUser().getUserid(), userid) : false);
		List<UserComment> comments = us.getCommentsByUser(userid, pageNum);
//		mav.addObject("comments", comments);
//		mav.addObject("commentQty", comments.size());
//		mav.addObject("currCommentPage", pageNum);
//		mav.addObject("maxCommentPage", us.getCommentsMaxPage(userid));
//		mav.addObject("totalCommentQty", us.countByUserComments(userid));
//		mav.addObject("commentsPageInitIndex", us.getCommentsPageInitIndex(pageNum));
		
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@RequestMapping(value = "/user/{userId}/comment", method = { RequestMethod.POST })
    public Object comment(@PathVariable("userId") long userId, 
    		@Valid @ModelAttribute("commentForm") final CommentForm form, final BindingResult errors,
			HttpServletRequest request) throws UserNotAuthorizedException, UserNotFoundException {
		
		if(errors.hasErrors()) {
    		return userProfile(userId, 1, form);
    	}
		
		us.createComment(loggedUser().getUserid(), userId, form.getComment());
		
	    return null;//new ModelAndView("redirect:/user/" + userId);
	}
	
	@RequestMapping("/")
	public Object index(@ModelAttribute("signupForm") final NewUserForm form) {
		if(cph.isAuthenticated()) {
			if(cph.isAdmin())
				return null;//new ModelAndView("redirect:/admin/");
			//return new ModelAndView("redirect:/home");
		}
		return null;//new ModelAndView("index");
	}
	
	@RequestMapping(value = "/user/create", method = { RequestMethod.POST })
	public Object create(
			@Valid @ModelAttribute("signupForm") final NewUserForm form,
			final BindingResult errors,
			HttpServletRequest request) {
		
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
			u = us.create(form.getUsername(), form.getFirstName(), form.getLastName(), 
					encodedPassword, Role.ROLE_USER, picture);

		} catch(PictureProcessingException | IOException e) {
			
			LOGGER.error("Error reading profile picture {}", profilePicture.getOriginalFilename());
			//ModelAndView mav = index(form);
			//mav.addObject("fileErrorMessage", profilePicture.getOriginalFilename());
			return null;//mav;

		} catch(UserAlreadyExistsException e) {

			LOGGER.warn("User tried to register with repeated email {}", form.getUsername());
			//ModelAndView mav = index(form);
			//mav.addObject("duplicateUsername", form.getUsername());
			return null;//mav;
		}
		
		ems.userRegistered(u, LocaleContextHolder.getLocale());
		cph.authenticate(u.getUsername(), u.getPassword(), request);
		return null;//new ModelAndView("redirect:/home");
	}
	
	@RequestMapping("/user/{userId}/picture")
	public void getUserProfilePicture(@PathVariable("userId") long userid,
			HttpServletResponse response) {
		Optional<ProfilePicture> picOptional = pps.findByUserId(userid);
		//response.setContentType(MediaType.IMAGE_PNG_VALUE);
		try {
			if(picOptional.isPresent()) {
				response.getOutputStream().write(picOptional.get().getData());
			}
			else {
				LOGGER.debug("Returning default profile picture");
				IOUtils.copy(new ClassPathResource(DEFAULT_PROFILE_PICTURE).getInputStream(), response.getOutputStream());
			}
		}
		catch(IOException e) {
			LOGGER.error("Reading of user #{}'s picture failed.", userid);
		}
	}
	
//	@ExceptionHandler({ UserNotFoundException.class })
//	private ModelAndView userNotFound() {
//		return new ModelAndView("404");
//	}

}
