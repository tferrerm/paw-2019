package ar.edu.itba.paw.webapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.interfaces.PitchPictureService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.PitchPicture;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.webapp.dto.EventCollectionDto;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.HourRangeDto;
import ar.edu.itba.paw.webapp.dto.PitchCollectionDto;
import ar.edu.itba.paw.webapp.dto.PitchDto;
import ar.edu.itba.paw.webapp.dto.SportCollectionDto;
import ar.edu.itba.paw.webapp.exception.PitchNotFoundException;

@Path("pitches")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class PitchController extends BaseController {

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

	@GET
	public Response listPitches(
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("name") String name,
			@QueryParam("sport") Sport sport,
			@QueryParam("location") String location,
			@QueryParam("club") String clubName) {

        List<Pitch> pitches = ps.findBy(
				Optional.ofNullable(name),
				Optional.ofNullable(sport),
				Optional.ofNullable(location),
				Optional.ofNullable(clubName),
				pageNum);
		
		int totalPitchQty = ps.countFilteredPitches(Optional.ofNullable(name), 
        		Optional.ofNullable(sport), Optional.ofNullable(location), 
        		Optional.ofNullable(clubName));
		int pageInitialIndex = ps.getPageInitialPitchIndex(pageNum);
        
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
		LOGGER.debug("Image for pitch {} presence: {}", pitchid, picOptional.isPresent());
		
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
	
    @GET
    @Path("/hour-range")
    public Response getHours() {
    	return Response.ok(HourRangeDto.ofHours(es.getMinHour(), es.getMaxHour())).build();
    }

}
