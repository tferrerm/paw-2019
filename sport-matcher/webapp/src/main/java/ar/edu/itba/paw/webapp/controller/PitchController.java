package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exception.EventCreationException;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchPictureService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.PitchCollectionDto;
import ar.edu.itba.paw.webapp.dto.PitchDto;
import ar.edu.itba.paw.webapp.dto.SportCollectionDto;
import ar.edu.itba.paw.webapp.dto.form.EventForm;
import ar.edu.itba.paw.webapp.dto.form.validator.FormValidator;
import ar.edu.itba.paw.webapp.exception.FormValidationException;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@Path("pitches")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class PitchController extends BaseController {

	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(PitchController.class);
	private static final String DEFAULT_PITCH_PICTURE = "pitch_default.png";
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private PitchService ps;
	
	@Autowired
	private PitchPictureService pps;
	
	@Autowired
	private EventService es;
	
	@Autowired
	private FormValidator validator;

	@GET
	public Response listPitches(
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("name") String name,
			@QueryParam("sport") Sport sport,
			@QueryParam("location") String location,
			@QueryParam("club") String clubName) {
		//String queryString = buildQueryString(name, sportString, location, clubName);
		//ModelAndView mav = new ModelAndView("pitchesList");
		
		//mav.addObject("pageNum", pageNum);
        //mav.addObject("queryString", queryString);
        //mav.addObject("sports", Sport.values());
        //mav.addObject("pageInitialIndex", ps.getPageInitialPitchIndex(pageNum));
        
        List<Pitch> pitches = ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				Optional.ofNullable(clubName),
				pageNum);
		//mav.addObject("pitches", pitches);
		//mav.addObject("pitchQty", pitches.size());
		
		int totalPitchQty = ps.countFilteredPitches(Optional.ofNullable(name), 
        		Optional.ofNullable(sport), Optional.ofNullable(location), 
        		Optional.ofNullable(clubName));
		int pageInitialIndex = ps.getPageInitialPitchIndex(pageNum);
		
        //mav.addObject("totalPitchQty", totalPitchQty);
        //mav.addObject("lastPageNum", ps.countPitchPages(totalPitchQty));
        
		return Response
				.ok(PitchCollectionDto.ofPitches(
						pitches.stream()
						.map(PitchDto::ofPitch)
						.collect(Collectors.toList()), totalPitchQty, ps.countPitchPages(totalPitchQty), pageInitialIndex))
				.build();
	}
	
	@GET
	@Path("/{id}")
	public Response getPitch(@PathParam("id") final long id) throws PitchNotFoundException {
		final Pitch pitch = ps.findById(id).orElseThrow(PitchNotFoundException::new);
		//System.out.println(es.getScheduleDaysHeader());
		//System.out.println(es.getAvailableHoursMap(MIN_HOUR, MAX_HOUR));
		//System.out.println(es.convertEventListToBooleanSchedule(es.findCurrentEventsInPitch(id)));
		
		return Response.ok(PitchDto.ofPitch(pitch)).build();
	}
	
	@GET
    @Produces(value = {
			org.springframework.http.MediaType.IMAGE_PNG_VALUE,
			org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
		  })
	@Path("/{id}/picture")
	public Response getPitchPicture(@PathParam("id") long pitchid) throws IOException {
		Optional<PitchPicture> picOptional = pps.findByPitchId(pitchid);

		final CacheControl cache = new CacheControl();
		cache.setNoTransform(false);
		cache.setMaxAge(2592000); // 1 month
		
		byte[] image = picOptional.isPresent() ? picOptional.get().getData()
				: IOUtils.toByteArray(new ClassPathResource(DEFAULT_PITCH_PICTURE).getInputStream());
		
		return Response.ok(image).cacheControl(cache).build();
	}
	
	@GET
	@Path("/sports")
	public Response getSports() {
		return Response.ok(SportCollectionDto.ofSports(Sport.values())).build();
	}
	
	@GET
	@Path("/{id}/week-events")
	public Response getPitchSchedule(@PathParam("id") final long id,
			@QueryParam("pageNum") @DefaultValue("1") int pageNum) throws PitchNotFoundException {
		//final Pitch pitch = ps.findById(id).orElseThrow(PitchNotFoundException::new);
		List<Event> weekEvents = es.findCurrentEventsInPitch(id);
		
		return Response.ok(EventCollectionDto.ofEvents(weekEvents.stream() // CAMBIAR METHOD
				.map(ev -> EventDto.ofEvent(ev, true)).collect(Collectors.toList()))).build();
	}
	
    @POST
    @Path("/{pitchId}/events/")
    public Response createEvent(
    		@PathParam("pitchId") long pitchId,
    		@FormDataParam("name") final String name,
    		@FormDataParam("description") final String description,
    		@FormDataParam("maxParticipants") final String maxParticipants,
    		@FormDataParam("date") final String date,
    		@FormDataParam("startsAtHour") final String startsAtHour,
    		@FormDataParam("endsAt") final String endsAtHour,
    		@FormDataParam("inscriptionEndDate") final String inscriptionEndDate)
    		 throws PitchNotFoundException, FormValidationException, EventCreationException {
    	
    	Integer mp = tryInteger(maxParticipants);
    	Integer sa = tryInteger(startsAtHour);
    	Integer ea = tryInteger(endsAtHour);
    	Instant eventDate = tryInstantStartOfDay(date, TIME_ZONE);
    	Instant inscriptionEnd = tryDateTimeToInstant(inscriptionEndDate, TIME_ZONE);
    	
    	validator.validate(
    		new EventForm()
    		.withName(name)
    		.withDescription(description)
    		.withMaxParticipants(mp)
    		.withDate(eventDate)
    		.withStartsAtHour(sa)
    		.withEndsAtHour(ea)
    		.withInscriptionEndDate(inscriptionEnd)
    	);
    	

    	Pitch p = ps.findById(pitchId).orElseThrow(PitchNotFoundException::new);
    	Event ev = null;
//    	try {
	    	ev = es.create(name, loggedUser(), p, description,
	    			mp, eventDate, sa, ea, inscriptionEnd);
//    	} catch(EndsBeforeStartsException e) {
//    		return eventCreationError("ends_before_starts", pitchId, form);
//    	} catch(DateInPastException e) { // NOOOO!!!!
//    		return eventCreationError("event_in_past", pitchId, form);
//    	} catch(MaximumDateExceededException e) {
//    		return eventCreationError("date_exceeded", pitchId, form);
//    	} catch(EventOverlapException e) {
//    		return eventCreationError("event_overlap", pitchId, form);
//    	} catch(HourOutOfRangeException e) {
//    		return eventCreationError("hour_out_of_range", pitchId, form);
//    	} catch(InscriptionDateInPastException e) { // NO!!!!!
//    		return eventCreationError("inscription_date_in_past", pitchId, form);
//    	} catch(InscriptionDateExceededException e) {
//    		return eventCreationError("inscription_date_exceeded", pitchId, form);
//    	}
    	return Response.ok().entity(EventDto.ofEvent(ev, false)).build();
    }

}
