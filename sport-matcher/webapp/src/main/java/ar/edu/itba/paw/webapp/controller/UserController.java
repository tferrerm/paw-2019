package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.io.InputStream;
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
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.ProfilePicture;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserComment;
import ar.edu.itba.paw.webapp.auth.TokenAuthenticationManager;
import ar.edu.itba.paw.webapp.dto.ClubDto;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.EventScheduleDto;
import ar.edu.itba.paw.webapp.dto.FullUserDto;
import ar.edu.itba.paw.webapp.dto.UserCommentCollectionDto;
import ar.edu.itba.paw.webapp.dto.UserCommentDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.form.CommentForm;
import ar.edu.itba.paw.webapp.dto.form.UserForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.CommentNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.http.CustomStatus;

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
	private FormValidator validator;

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

//	@GET
//	@Path("/users/login")
//	public Response login(@RequestParam(name = "error", defaultValue = "false") boolean error) {
//		if(cph.isAuthenticated()) {
//			if(cph.isAdmin())
//				return null;//return new ModelAndView("redirect:/admin/");
//			//return new ModelAndView("redirect:/home");
//		}
//		//ModelAndView mav = new ModelAndView("login");
//		//mav.addObject("error", error);
//		return null;//mav;
//	}
	
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
		final int currentEventCount = es.countByUserInscriptions(true, userid);
		final Sport favoriteSport = es.getFavoriteSport(userid).orElse(null);
		final int currEventsOwned = es.countByOwner(true, userid);
		final int pastEventsParticipant = es.countByUserInscriptions(false, userid);
		final Club mainClub = es.getFavoriteClub(userid).orElse(null);
		final int votesReceived = us.countVotesReceived(userid);

		return Response.ok(FullUserDto.ofUser(user, currentEventCount, favoriteSport,
				currEventsOwned, pastEventsParticipant, ClubDto.ofClub(mainClub), votesReceived))
				.build();
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
    	int commentCount = us.countByUserComments(userid);
    	int totalPages = us.getCommentsMaxPage(userid);
    	int commentsPageInitIndex = us.getCommentsPageInitIndex(pageNum);

    	return Response.ok(UserCommentCollectionDto.ofComments(
    			comments.stream().map(UserCommentDto::ofComment).collect(Collectors.toList()),
    			commentCount, totalPages, commentsPageInitIndex)
    			).build();
    }
    
	
    @POST
	@Path("/{id}/comment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response comment(@PathParam("id") long userId, 
    		@FormDataParam("comment") final String commentContent)
			throws UserNotAuthorizedException, UserNotFoundException, FormValidationException {

    	CommentForm cf = new CommentForm().withComment(commentContent);
    	validator.validate(cf);

    	// HARDCODED HARDCODEADO
		UserComment comment = us.createComment(/*loggedUser().getUserid()*/2, userId, commentContent);

		// Absolute: /users/
		// "/users/1/comments/"
		final URI uri = uriInfo.getAbsolutePathBuilder()
				.path(userId + "/comments/" + comment.getCommentId()).build();
	    return Response.created(uri).entity(UserCommentDto.ofComment(comment)).build();
	}
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = { MediaType.APPLICATION_JSON })
	public Response createUser(@FormDataParam("username") final String username,
							   @FormDataParam("password") final String password,
							   @FormDataParam("firstname") final String firstname,
							   @FormDataParam("lastname") final String lastname,
							   @FormDataParam("picture") final InputStream profilePicture) throws FormValidationException {
    	
    	validator.validate(new UserForm()
    			.withUsername(username)
    			.withPassword(password)
    			.withFirstname(firstname)
    			.withLastname(lastname));

		final User user;		
		final String encodedPassword = passwordEncoder.encode(password);
		
		try {
			byte[] picture = {};
			if(profilePicture != null)
				picture = IOUtils.toByteArray(profilePicture);
			user = us.create(username, firstname, lastname,
					encodedPassword, Role.ROLE_USER, picture);

		} catch(PictureProcessingException | IOException e) {

			LOGGER.error("Error reading profile picture from {}", username);
			return Response.status(CustomStatus.UNPROCESSABLE_ENTITY).build();

		} catch(UserAlreadyExistsException e) {

			LOGGER.warn("User tried to register with repeated email {}", username);
			return Response.status(Status.CONFLICT).build();
		}
		
		ems.userRegistered(user, LocaleContextHolder.getLocale());

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
    
    @GET
    @Path("/{id}/future-inscriptions")
    public Response getFutureInscriptedEvents(@PathParam("id") final long userid) {
    	if(loggedUser() == null || loggedUser().getUserid() != userid) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
    	List<Event> upcomingEvents = es.findFutureUserInscriptions(loggedUser().getUserid(), true);
		List<List<Event>> myEvents = es.convertEventListToSchedule(upcomingEvents);
		
		return Response.ok(EventScheduleDto.ofEvents(
				myEvents.stream()
				.map(x -> {
					return EventCollectionDto.ofEvents(x.stream()
							.map(ev -> ev != null? EventDto.ofEvent(ev, false) : null)
							.collect(Collectors.toList()));
				})
				.collect(Collectors.toList()))
				).build();
    }
    
    @GET
    @Path("/{id}/future-owned-events")
    public Response getMyFutureEvents(@PathParam("id") final long userid,
    		@QueryParam("pageNum") @DefaultValue("1") final int pageNum) {
    	if(loggedUser() == null || loggedUser().getUserid() != userid) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
		final List<Event> futureEvents = es.findByOwner(true, userid, pageNum);
		final int futureEventQty = es.countByOwner(true, userid);
		final int pageCount = es.countUserOwnedPages(true, userid);
		return Response.ok(
				EventCollectionDto.ofEvents(
						futureEvents.stream().map(e -> EventDto.ofEvent(e, true)).collect(Collectors.toList()),
						futureEventQty, pageCount, es.getPageInitialEventIndex(pageNum))
				).build();
    }
    
    @GET
    @Path("/{id}/past-owned-events")
    public Response getMyPastEvents(@PathParam("id") final long userid,
    		@QueryParam("pageNum") @DefaultValue("1") final int pageNum) {
    	if(loggedUser() == null || loggedUser().getUserid() != userid) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
		final List<Event> pastEvents = es.findByOwner(false, userid, pageNum);
		final int pastEventQty = es.countByOwner(false, userid);
		final int pageCount = es.countUserOwnedPages(false, userid);
		return Response.ok(
				EventCollectionDto.ofEvents(
						pastEvents.stream().map(e -> EventDto.ofEvent(e, false)).collect(Collectors.toList()),
						pastEventQty, pageCount, es.getPageInitialEventIndex(pageNum))
				).build();
    }
    
    @GET
    @Path("/{id}/history")
    public Response getHistory(@PathParam("id") final long userid,
    		@QueryParam("pageNum") @DefaultValue("1") final int pageNum) {
    	if(loggedUser() == null || loggedUser().getUserid() != userid) {
    		return Response.status(Status.FORBIDDEN).build();
    	}
    	
		final long loggedUserId = loggedUser().getUserid();
		final List<Event> events = es.findPastUserInscriptions(loggedUserId, pageNum);
		final int eventQty = es.countByUserInscriptions(false, loggedUserId);
		final int pageCount = es.countUserInscriptionPages(false, loggedUserId);
		return Response.ok(
				EventCollectionDto.ofEvents(
						events.stream().map(e -> EventDto.ofEvent(e, false)).collect(Collectors.toList()),
						eventQty, pageCount, es.getPageInitialEventIndex(pageNum))
				).build();
    }
	
//	@ExceptionHandler({ UserNotFoundException.class })
//	private ModelAndView userNotFound() {
//		return new ModelAndView("404");
//	}

}
