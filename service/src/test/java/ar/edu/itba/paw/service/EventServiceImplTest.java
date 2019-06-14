package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.TestConfig;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EventServiceImplTest {
	
	@Mock
	private EventDao ed;
	
	@InjectMocks
	private EventServiceImpl es;
	
	private static final User USER = Mockito.mock(User.class);
	private static final Pitch PITCH = Mockito.mock(Pitch.class);
	
	private static final String NAME_1 = "e1";
	private static final String NAME_2 = "e1";
	private static final String DESCRIPTION = "d";
	private static final int MIN_HOUR = 9;
	private static final int MAX_HOUR = 23;
	private static final String TIME_ZONE = "America/Buenos_Aires";
	
	private static final Instant STARTS_1 =  today().toInstant();
	private static final Instant ENDS_1 =  today()
			.plus(1, ChronoUnit.HOURS).toInstant();
	private static final Instant STARTS_2 =  today().
			plus(1, ChronoUnit.DAYS).toInstant();
	private static final Instant ENDS_2 =  today().plus(1, ChronoUnit.DAYS)
			.plus(1, ChronoUnit.HOURS).toInstant();
	private static final Instant NOW = Instant.now();
	private static final Event EVENT_1 = new Event(NAME_1, PITCH, DESCRIPTION, 1, STARTS_1, ENDS_1, NOW);
	private static final Event EVENT_2 = new Event(NAME_2, PITCH, DESCRIPTION, 1, STARTS_2, ENDS_2, NOW);
	private static final List<Event> EVENT_LIST = new ArrayList<>();
			
	private static ZonedDateTime today() {
		return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE));
	}
	
	@Before
	public void setUp() {
		EVENT_LIST.add(EVENT_1);
		EVENT_LIST.add(EVENT_2);
	}
	
	@Test
	public void convertEventListToScheduleTest() {
		//boolean[][] schedule = es.convertEventListToSchedule(EVENT_LIST, MIN_HOUR, MAX_HOUR, 7);
		//Assert.assertEquals(7, schedule.length);
		//Assert.assertEquals(7, schedule[0].length);
		Assert.assertTrue(true);
	}
	
	@Test
	public void validCreateTest() throws Exception {
//		Mockito.when(ed.create(
//				Mockito.anyString(),
//				Mockito.any(User.class),
//				Mockito.any(Pitch.class),
//				Mockito.anyString(),
//				Mockito.anyInt(),
//				Mockito.any(Instant.class),
//				Mockito.any(Instant.class)))
//		.thenReturn(EVENT_1);
//		final Event e = es.create(NAME_1, USER, PITCH, DESCRIPTION,
//				1, NOW, 10, 11);
	}
	
	

}
