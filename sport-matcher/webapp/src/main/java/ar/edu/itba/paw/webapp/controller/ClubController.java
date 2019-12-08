package ar.edu.itba.paw.webapp.controller;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.webapp.dto.ClubCollectionDto;
import ar.edu.itba.paw.webapp.dto.ClubCommentCollectionDto;
import ar.edu.itba.paw.webapp.dto.ClubCommentDto;
import ar.edu.itba.paw.webapp.dto.ClubDto;
import ar.edu.itba.paw.webapp.dto.FullClubDto;
import ar.edu.itba.paw.webapp.dto.PitchCollectionDto;
import ar.edu.itba.paw.webapp.dto.PitchDto;
import ar.edu.itba.paw.webapp.dto.form.CommentForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.ClubNotFoundException;
import ar.edu.itba.paw.webapp.exception.CommentNotFoundException;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Path("clubs")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class ClubController extends BaseController {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClubController.class);
	
	@Autowired
	private FormValidator validator;
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private ClubService cs;
	
	@Autowired
	private PitchService ps;
	
	@GET
	@Path("/{id}")
	public Response showClub(@PathParam("id") long clubid) throws ClubNotFoundException {
		
		final Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		final boolean haveRelationship = loggedUser() != null ?
				cs.haveRelationship(loggedUser().getUserid(), clubid) : false;
		
		return Response
				.status(Status.OK)
				.entity(FullClubDto.ofClub(club, cs.countPastEvents(clubid), haveRelationship))
				.build();
	}
	
	@GET
	@Path("/{id}/pitches")
	public Response getClubPitches(@PathParam("id") long clubid,
			@QueryParam("pageNum") @DefaultValue("1") int pageNum) throws ClubNotFoundException {
		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
		
		List<Pitch> pitches = ps.findByClubId(clubid, pageNum);

		// TODO: IMPLEMENTAR PARA OBTENER PITCHES ESPECIFICOS DEL CLUB (BUSCAR POR ID)
		int pitchCount = ps.countFilteredPitches(
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(club.getName()));
		int pageCount = ps.countPitchPages(pitchCount);
		
		return Response
				.status(Status.OK)
				.entity(PitchCollectionDto.ofPitches(
						pitches.stream()
						.map(PitchDto::ofPitch)
						.collect(Collectors.toList()), pitchCount, pageCount))
				.build();
		
	}
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/{id}/comment")
    public Response comment(@PathParam("id") long clubId,
    		@FormDataParam("comment") final String commentContent)
    				throws UserNotAuthorizedException, ClubNotFoundException, FormValidationException {
		
		final CommentForm form = new CommentForm().withComment(commentContent);
		validator.validate(form);

		final ClubComment comment = cs.createComment(loggedUser().getUserid(), clubId, commentContent);

		final URI uri = uriInfo.getAbsolutePathBuilder()
				.path(clubId + "/comments/" + comment.getCommentId()).build();
	    return Response
	    		.created(uri)
	    		.entity(ClubCommentDto.ofComment(comment))
	    		.build();
	}
	
	@GET
    @Path("/{id}/comment/{commentId}")
    public Response getComment(@PathParam("id") long clubid,
    						   @PathParam("commentId") long commentId)
    								   throws UserNotFoundException, CommentNotFoundException {
    	cs.findById(clubid).orElseThrow(UserNotFoundException::new);
    	ClubComment comment = cs.findComment(commentId).orElseThrow(CommentNotFoundException::new);

    	return Response.ok(ClubCommentDto.ofComment(comment)).build();
    }
	
	@GET
	@Path("/{id}/comments")
	public Response getComments(@PathParam("id") long clubid,
								@QueryParam("pageNum") @DefaultValue("1") int pageNum) throws ClubNotFoundException {
		List<ClubComment> comments = cs.getCommentsByClub(clubid, pageNum);
		int commentCount = cs.countByClubComments(clubid);
		int pageCount = cs.getCommentsMaxPage(clubid);
		int commentsPageInitIndex = cs.getCommentsPageInitIndex(pageNum);
		
		return Response
				.status(Status.OK)
				.entity(ClubCommentCollectionDto.ofComments(
						comments.stream()
							.map(ClubCommentDto::ofComment)
							.collect(Collectors.toList()), commentCount, pageCount, commentsPageInitIndex))
				.build();
	}

	@GET
	public Response clubs(
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("name") String clubName,
            @QueryParam("location") String location) {
        
        final List<Club> clubs = cs.findBy(
				Optional.ofNullable(clubName), 
        		Optional.ofNullable(location),
        		pageNum);
        
        int totalClubQty = cs.countFilteredClubs(
        		Optional.ofNullable(clubName), 
        		Optional.ofNullable(location));
        int clubPages = cs.countClubPages(totalClubQty);
        int pageInitialIndex = cs.getPageInitialClubIndex(pageNum);

		return Response
				.status(Status.OK)
				.entity(ClubCollectionDto.ofClubs(
						clubs.stream().map(ClubDto::ofClub).collect(Collectors.toList()),
						totalClubQty, clubPages, pageInitialIndex))
				.build();
	}
	
}
