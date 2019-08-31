package ar.edu.itba.paw.webapp.controller.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.PitchService;
import ar.edu.itba.paw.webapp.controller.BaseController;

@Path("admin/clubs")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class AdminClubController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminClubController.class);
	
	@Context
	private	UriInfo	uriInfo;
	
	@Autowired
	private ClubService cs;

	@Autowired
	private PitchService ps;
	
	@GET
	@Path("/{clubId}")
	public Response gaga() {
		return null;
	}

	/*
//	@GET
//	@Path("/{clubId}")
//	public ModelAndView showClub(@PathParam("clubId") long clubid, @ModelAttribute("newPitchForm") final NewPitchForm form)
//			throws ClubNotFoundException {
//
//		List<Pitch> pitches = ps.findByClubId(clubid, 1);
//		ModelAndView mav = new ModelAndView("admin/club");
//		mav.addObject("newPitchForm", form);
//		mav.addObject("sports", Sport.values());
//		mav.addObject("pitches", pitches);
//		Club club = cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
//		mav.addObject("club", club);
//		return mav;
//	}
//
//	@GET
//	@Path("/{pageNum}")
//	public ModelAndView clubs(@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form,
//			@PathParam("pageNum") int pageNum,
//			@RequestParam(value = "name", required = false) String clubName,
//            @RequestParam(value = "location", required = false) String location) {
//		
//		String queryString = buildQueryString(clubName, location);
//		
//		ModelAndView mav = new ModelAndView("admin/clubList");
//		
//		mav.addObject("pageNum", pageNum);
//        mav.addObject("queryString", queryString);
//        mav.addObject("pageInitialIndex", cs.getPageInitialClubIndex(pageNum));
//        
//        List<Club> clubs = cs.findBy(
//				Optional.ofNullable(clubName), 
//        		Optional.ofNullable(location), 
//        		pageNum);
//        mav.addObject("clubs", clubs);
//        mav.addObject("clubQty", clubs.size());
//        
//        Integer totalClubQty = cs.countFilteredClubs(Optional.ofNullable(clubName), 
//        		Optional.ofNullable(location));
//        mav.addObject("totalClubQty", totalClubQty);
//        mav.addObject("lastPageNum", cs.countClubPages(totalClubQty));
//        
//		return mav;
//	}
//	
	@GET
	@Path("/filter")
    public Response applyFilter(@ModelAttribute("clubsFiltersForm") final ClubsFiltersForm form) {
    	String name = form.getName();
    	String location = form.getLocation();
        String queryString = buildQueryString(name, location);
        return null;//new ModelAndView("redirect:/admin/clubs/1" + queryString);
    }

    private String buildQueryString(final String name, final String location){
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("?");
	    if(name != null && !name.isEmpty()) {
        	strBuilder.append("name=").append(encodeUriString(name)).append("&");
        }
        if(location != null && !location.isEmpty()) {
        	strBuilder.append("location=").append(encodeUriString(location));
        } else {
        	strBuilder.deleteCharAt(strBuilder.length()-1);
        }
        return strBuilder.toString();
    }
//	
//    @GET
//	@Path("/new")
//	public ModelAndView newClub(@ModelAttribute("newClubForm") final NewClubForm form) {
//		return new ModelAndView("admin/newClub");
//	}
//
//    @POST
//	@Path("/")
//	public ModelAndView createClub(
//			@Valid @ModelAttribute("newClubForm") final NewClubForm form,
//			final BindingResult errors,
//			HttpServletRequest request) {
//		
//		Club c = cs.create(form.getName(), form.getLocation());
//		LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
//		
//		return new ModelAndView("redirect:/admin/club/" + c.getClubid());
//	}
//
//    @POST
//	@Path("/{clubId}/pitch/create")
//	public ModelAndView createPitch(
//			@Valid @ModelAttribute("newPitchForm") final NewPitchForm form,
//			final BindingResult errors,
//			HttpServletRequest request,
//			@PathParam("clubId") final long clubId) 
//					throws ClubNotFoundException {
//		
//		Sport sport = null;
//		try {
//			sport = Sport.valueOf(form.getSport());
//		} catch(IllegalArgumentException e) {
//			LOGGER.warn("Unable to convert sport to enum");
//			errors.rejectValue("sport", "sport_not_in_list");
//		}
//		
//		if(errors.hasErrors()) {
//			return showClub(clubId, form);
//		}
//		
//		final MultipartFile pitchPicture = form.getPitchPicture();
//		Club c = cs.findById(clubId).orElseThrow(ClubNotFoundException::new);
//		
//		try {
//			byte[] picture = pitchPicture.getBytes();
//			ps.create(c, form.getName(), sport, picture);
//			LOGGER.debug("Club {} with id {} created", c.getName(), c.getClubid());
//		} catch(PictureProcessingException | IOException e) {
//			LOGGER.error("Error reading pitch picture {}", pitchPicture.getOriginalFilename());
//			ModelAndView mav = showClub(c.getClubid(), form);
//			mav.addObject("fileErrorMessage", pitchPicture.getOriginalFilename());
//			return mav;
//		}
//		
//		return new ModelAndView("redirect:/admin/club/" + clubId);
//	}
//	
//    @DELETE
//	@Path("/{id}/")
//	public ModelAndView deleteClub(@PathParam("id") final long clubid) 
//		throws ClubNotFoundException {
//		cs.findById(clubid).orElseThrow(ClubNotFoundException::new);
//		cs.deleteClub(clubid);
//		return new ModelAndView("redirect:/admin/clubs/1");
//	}
//	
////	@ExceptionHandler({ ClubNotFoundException.class })
////	public ModelAndView clubNotFound() {
////		return new ModelAndView("404");
////	}*/

}
