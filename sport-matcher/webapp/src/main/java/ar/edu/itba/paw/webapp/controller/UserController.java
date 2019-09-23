package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.itba.paw.exception.PictureProcessingException;
import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;
import ar.edu.itba.paw.webapp.auth.TokenAuthenticationManager;
import ar.edu.itba.paw.webapp.dto.UserCommentCollectionDto;
import ar.edu.itba.paw.webapp.dto.UserCommentDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.form.UserForm;
import ar.edu.itba.paw.webapp.exception.CommentNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

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
	private TokenAuthenticationManager cph;

	@GET
	@Path("/users/login")
	public Response login(@RequestParam(name = "error", defaultValue = "false") boolean error) {
		if(cph.isAuthenticated()) {
			if(cph.isAdmin())
				return null;//return new ModelAndView("redirect:/admin/");
			//return new ModelAndView("redirect:/home");
		}
		//ModelAndView mav = new ModelAndView("login");
		//mav.addObject("error", error);
		return null;//mav;
	}
	
//	@GET
//    @Path("/logout")
//    public Response logout(HttpServletRequest request, HttpServletResponse response) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth != null) {
//            new SecurityContextLogoutHandler().logout(request,response,auth);
//        }
//
//        request.getSession().invalidate();
////        return new ModelAndView("login");
//        return null;
//    }
	
    @GET
    @Path("/{id}")
	public Response userProfile(@PathParam("id") long userid) throws UserNotFoundException {
		
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
//		List<UserComment> comments = us.getCommentsByUser(userid, pageNum);
//		mav.addObject("comments", comments);
//		mav.addObject("commentQty", comments.size());
//		mav.addObject("currCommentPage", pageNum);
//		mav.addObject("maxCommentPage", us.getCommentsMaxPage(userid));
//		mav.addObject("totalCommentQty", us.countByUserComments(userid));
//		mav.addObject("commentsPageInitIndex", us.getCommentsPageInitIndex(pageNum));
		
		final User user = us.findById(userid).orElseThrow(UserNotFoundException::new);
		
		return Response.ok(UserDto.ofUser(user)).build();
	}
    
    @GET
    @Path("/{id}/comment/{commentId}")
    public Response getComment(@PathParam("id") long userid,
    						   @PathParam("commentId") long commentId)
    								   throws UserNotFoundException, CommentNotFoundException {
    	us.findById(userid).orElseThrow(UserNotFoundException::new);
    	UserComment comment = us.getComment(commentId).orElseThrow(CommentNotFoundException::new);

    	return Response.ok(UserCommentDto.ofComment(comment)).build();
    }
    
    @GET
    @Path("/{id}/comments")
    public Response getComments(@PathParam("id") long userid,
    			@QueryParam("page") @DefaultValue("1") int pageNum) throws UserNotFoundException {
    	us.findById(userid).orElseThrow(UserNotFoundException::new);
    	List<UserComment> comments = us.getCommentsByUser(userid, pageNum);
    	int totalPages = us.getCommentsMaxPage(userid);

    	return Response.ok(UserCommentCollectionDto.ofComments(
    			comments.stream().map(UserCommentDto::ofComment).collect(Collectors.toList()),
    			totalPages)
    			).build();
    }
    
	
//    @POST
//	@Path("/{id}/comment")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response comment(@PathParam("id") long userId, 
//    		@FormDataParam("commentForm") final CommentForm form)
//			throws UserNotAuthorizedException, UserNotFoundException {
//		
//    	// Validator VALIDAR!!!!!!!!!!
//    	
//		//if(errors.hasErrors()) {
//    		//return userProfile(userId, 1, form);
//    	//}
//		
//		UserComment comment = us.createComment(loggedUser().getUserid(), userId, form.getComment());
//		
//		// Absolute: /users/
//		// "/users/1/comments/"
//		final URI uri = uriInfo.getAbsolutePathBuilder()
//				.path(userId + "/comments/" + comment.getCommentId()).build();
//	    return Response.created(uri).entity(UserCommentDto.ofComment(comment)).build();
//	}
	
//    @GET
//	@Path("/")
//	public Response index(@ModelAttribute("signupForm") final NewUserForm form) {
//		if(cph.isAuthenticated()) {
//			if(cph.isAdmin())
//				return null;//new ModelAndView("redirect:/admin/");
//			//return new ModelAndView("redirect:/home");
//		}
//		return null;//new ModelAndView("index");
//	}
    
    @POST
	@Path("/create")
    @Consumes(value = { MediaType.MULTIPART_FORM_DATA, })
    @Produces(value = { MediaType.APPLICATION_JSON })
	public Response createUser(@FormDataParam("user") final UserForm form) {
		
    	// DTOValidator.VALIDATE!!!!!!!!!!!
    	
    	// AUTO LOGIN
    	
    	
//		if(!form.repeatPasswordMatching())
//		 	errors.rejectValue("repeatPassword", "different_passwords");
//		if(errors.hasErrors()) {
//			//return index(form);
//		}
    	
    	if(form == null)
    		return Response.status(Status.BAD_REQUEST).build();
		
		User user;
		//final MultipartFile profilePicture = form.getProfilePicture();
		final String encodedPassword = passwordEncoder.encode(form.getPassword());
		
		try {
			//byte[] picture = profilePicture.getBytes();
			user = us.create(form.getUsername(), form.getFirstname(), form.getLastname(),
					encodedPassword, Role.ROLE_USER, null);

		} catch(PictureProcessingException e) {//| IOException e) {
			
			//LOGGER.error("Error reading profile picture {}", profilePicture.getOriginalFilename());
			//ModelAndView mav = index(form);
			//mav.addObject("fileErrorMessage", profilePicture.getOriginalFilename());
			return Response.status(Status.BAD_REQUEST).build();//mav;

		} catch(UserAlreadyExistsException e) {

			LOGGER.warn("User tried to register with repeated email {}", form.getUsername());
			//ModelAndView mav = index(form);
			//mav.addObject("duplicateUsername", form.getUsername());
			return Response.status(Status.BAD_REQUEST).build();//mav;
		}
		
		ems.userRegistered(user, LocaleContextHolder.getLocale());
		cph.authenticate(user.getUsername(), user.getPassword(), null);
		final URI uri = uriInfo.getAbsolutePathBuilder().path(user.getUsername()).build();
		return Response.created(uri).entity(UserDto.ofUser(user)).build();
	}
	
    @GET
	@Path("/{id}/picture")
    @Produces(value = {
    					org.springframework.http.MediaType.IMAGE_PNG_VALUE,
    					org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
    				  })
	public Response getUserProfilePicture(@PathParam("id") long userid) throws IOException {
		Optional<ProfilePicture> picOptional = pps.findByUserId(userid);
		
		final CacheControl cache = new CacheControl();
		cache.setNoTransform(false);
		cache.setMaxAge(2592000); // 1 month
		
		byte[] image = picOptional.isPresent() ? picOptional.get().getData() :
			IOUtils.toByteArray(new ClassPathResource(DEFAULT_PROFILE_PICTURE).getInputStream());
		
		return Response.ok(image).cacheControl(cache).build();
		
	}
	
//	@ExceptionHandler({ UserNotFoundException.class })
//	private ModelAndView userNotFound() {
//		return new ModelAndView("404");
//	}

}
