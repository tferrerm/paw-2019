package ar.edu.itba.paw.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.EventFullException;
import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.exception.UserBusyException;
import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.interfaces.EventService;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventDao ed;
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final Map<DayOfWeek, Integer> DAYS_OF_WEEK_NUM = new HashMap<>();
	private static final String[] DAYS_OF_WEEK_ABR = {"day_mon", "day_tue", "day_wed", "day_thu", 
			"day_fri", "day_sat", "day_sun"};
	private static final int EVENT_ID_INDEX = 0;
	private static final int EVENT_INSCRIPTIONS_INDEX = 1;
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";

	@Override
	public Optional<Event> findByEventId(long eventid) {
		if(eventid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ed.findByEventId(eventid);
	}

	@Override
	public List<Event> findByUsername(boolean futureEvents, String username, int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return ed.findByUsername(futureEvents, username, pageNum);
	}
	
	@Override
	public List<Event> findCurrentEventsInPitch(final long pitchid) {
		if(pitchid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ed.findCurrentEventsInPitch(pitchid);
	}
	
	@Override
	public boolean[][] convertEventListToSchedule(List<Event> events, int minHour, 
			int maxHour, int dayAmount) {
		if(maxHour - minHour <= 0)
			return null;
		boolean[][] schedule = new boolean[maxHour - minHour][dayAmount];
		
		for(Event event : events) {
			DayOfWeek startsAtDayOfWeek = event.getStartsAt().atZone(ZoneId.of(TIME_ZONE))
					.toLocalDate().getDayOfWeek();
			DayOfWeek currentDayOfWeek = LocalDate.now(ZoneId.of(TIME_ZONE)).getDayOfWeek();
			
			Map<DayOfWeek, Integer> daysOfWeek = getDaysOfWeek();
			
			int dayIndex = (daysOfWeek.get(startsAtDayOfWeek) - daysOfWeek.get(currentDayOfWeek)) % 7; // Should change if dayAmount != 7
			if(dayIndex < 0)
				dayIndex += 7;
			
			int initialHourIndex = event.getStartsAt().atZone(ZoneId.of(TIME_ZONE))
					.toLocalDateTime().getHour() - minHour;
			int finalHourIndex = event.getEndsAt().atZone(ZoneId.of(TIME_ZONE))
					.toLocalDateTime().getHour() - minHour;
			
			for(int i = initialHourIndex; i < finalHourIndex; i++) {
				schedule[i][dayIndex] = true;
			}
		}
		return schedule;
	}
	
	private Map<DayOfWeek, Integer> getDaysOfWeek() {
		if(DAYS_OF_WEEK_NUM.isEmpty()) {
			int i = 0;
			for(DayOfWeek dow : DayOfWeek.values()) {
				DAYS_OF_WEEK_NUM.put(dow, i);
				i++;
			}
		}
		return DAYS_OF_WEEK_NUM;
	}
	
	public String[] getScheduleDaysHeader() {
		Map<DayOfWeek, Integer> daysOfWeek = getDaysOfWeek();
		int currDayOfWeek = daysOfWeek.get(LocalDate.now(ZoneId.of(TIME_ZONE)).getDayOfWeek());
		String[] nextSevenDays = new String[7];
		for(int i = 0; i < 7; i++) {
			if(i < currDayOfWeek) {
				nextSevenDays[7 - currDayOfWeek + i] = DAYS_OF_WEEK_ABR[i];
			} else {
				nextSevenDays[i - currDayOfWeek] = DAYS_OF_WEEK_ABR[i];
			}
		}
		return nextSevenDays;
	}
	
	@Override
	public Map<Event, Long> findByWithInscriptions(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<Sport> sport, Optional<Integer> vacancies, int page) {
		List<Event> events = findBy(onlyFuture, name, establishment, sport, vacancies, page);
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		List<Long[]> eventInscriptions = ed.countBy(onlyFuture, name, establishment,
				Optional.ofNullable(sportString), vacancies, page);
		events.sort(Comparator.comparing(Event::getEventId));
		eventInscriptions.sort(Comparator.comparing(arr -> arr[EVENT_ID_INDEX]));
		Map<Event, Long> eventWithInscriptions = new HashMap<>();
		for(int i = 0; i < events.size(); i++) {
			eventWithInscriptions.put(events.get(i), eventInscriptions.get(i)[EVENT_INSCRIPTIONS_INDEX]);
		}
		return eventWithInscriptions;
	}
	
	@Override
	public List<Event> findBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<Sport> sport, Optional<Integer> vacancies, int page) {
		if(page <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		
		return ed.findBy(onlyFuture, name, establishment, Optional.ofNullable(sportString),
				vacancies, page);
	}
	
	/*public List<Long[]> countBy(boolean onlyFuture, Optional<String> name, Optional<String> establishment,
			Optional<String> sport, Optional<Integer> vacancies, int page) {
		if(page <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		String sportString = null;
		if(sport.isPresent()) {
			sportString = sport.get().toString();
		}
		
		return ed.countBy(onlyFuture, name, establishment, Optional.ofNullable(sportString),
				vacancies, page);
	}*/
	
	@Override
	public int countUserEventPages(long userid) {
		if(userid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ed.countUserEventPages(userid);
	}
	
	@Override
	public List<Event> findFutureEvents(int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return ed.findFutureEvents(pageNum);
	}
	
	@Override
	public int countFutureEventPages() {
		return ed.countFutureEventPages();
	}

	@Transactional
	@Override
	public Event create(String name, User owner, Pitch pitch, String description,
			int maxParticipants, Instant startsAt, Instant endsAt) {
		return ed.create(name, owner, pitch, description, maxParticipants, startsAt, endsAt);
	}

	@Transactional
	@Override
	public boolean joinEvent(final User user, final Event event)
			throws UserAlreadyJoinedException, EventFullException, UserBusyException {
		
		// si no tiro excepcion y hago metodo separado, no obligo a validar esto
		if(countParticipants(event.getEventId()) + 1 > event.getMaxParticipants()) {
			throw new EventFullException(); 
		}
		
		return ed.joinEvent(user, event);
	}
	
	@Transactional
	@Override
	public void leaveEvent(final User user, final Event event) {
		ed.leaveEvent(user, event);
	}
	
	@Transactional
	@Override
	public void kickFromEvent(final User owner, final long kickedUserId, final Event event) 
		throws UserNotAuthorizedException {
		if(owner.getUserid() != event.getOwner().getUserid())
			throw new UserNotAuthorizedException("User is not the owner of the event.");
		ed.kickFromEvent(kickedUserId, event.getEventId());
	}
	
	@Override
	public int countParticipants(final long eventid) {
		if(eventid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return ed.countParticipants(eventid);
	}

	@Override
	public List<User> findEventUsers(final long eventid, final int pageNum) {
		if(eventid <= 0 || pageNum <= 0) {
			throw new IllegalArgumentException("Parameters must be greater than zero.");
		}
		return ed.findEventUsers(eventid, pageNum);
	}
	
	@Override
	public Map<Integer, String> getAvailableHoursMap(int minHour, int maxHour) {
		Map<Integer, String> availableHoursMap = new HashMap<>();
		for(int i = minHour; i < maxHour; i++) {
			availableHoursMap.put(i, i + ":00");
		}
		return availableHoursMap;
	}
	
	@Override
	public int countUserEvents(boolean isCurrentEventsQuery, final long userid) {
		return ed.countUserEvents(isCurrentEventsQuery, userid);
	}
	
	@Override
	public int countUserOwnedCurrEvents(final long userid) {
		return ed.countUserOwnedCurrEvents(userid);
	}
	
	@Override
	public List<Sport> getFavoriteSport(final long userid) {
		return ed.getFavoriteSport(userid);
	}
	
	@Override
	public List<Club> getFavoriteClub(final long userid) {
		return ed.getFavoriteClub(userid);
	}
	
	@Override
	public void deleteEvent(long eventid) {
		if(eventid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		ed.deleteEvent(eventid);
	}

}
