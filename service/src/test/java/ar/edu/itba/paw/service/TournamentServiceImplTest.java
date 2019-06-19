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

import ar.edu.itba.paw.exception.InscriptionDateInPastException;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TournamentServiceImplTest {
	
	@InjectMocks
	private TournamentServiceImpl ts;
	
	@Mock
	private TournamentDao td;
	
	@Mock
	private ClubService cd;
	
	private static final String TIME_ZONE = "America/Buenos_Aires";
	private static final String TNAME = "Tournament";
	private static final Sport SPORT = Sport.SOCCER;
	private static final Club CLUB = new Club(1, "name", "loc", Instant.now());
	private static final List<Pitch> PITCHES = new ArrayList<>();
	private static final int MAX_TEAMS = 4;
	private static final int TEAM_SIZE = 3;
	private static final Instant STARTS = today().toInstant().plus(2, ChronoUnit.DAYS)
			.plus(10, ChronoUnit.HOURS);
	private static final int STARTS_AT = 10;
	private static final Instant ENDS = Instant.now().plus(2, ChronoUnit.DAYS)
			.plus(12, ChronoUnit.HOURS);
	private static final int ENDS_AT = 12;
	private static final Instant INSCR_ENDS = Instant.now().plus(5, ChronoUnit.HOURS);
	private static final User USER = Mockito.mock(User.class);
	private static final Tournament TMOCK = Mockito.mock(Tournament.class);
	
	private static ZonedDateTime today() {
		return LocalDate.now().atStartOfDay(ZoneId.of(TIME_ZONE));
	}
	
	@Before
	public void setUp() {
		for(int i = 0; i < 3; i++) {
			PITCHES.add(Mockito.mock(Pitch.class));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createTournamentTest() throws Exception {
		Mockito.when(td.create(
				Mockito.anyString(),
				Mockito.any(Sport.class),
				Mockito.any(Club.class),
				Mockito.any(List.class),
				Mockito.anyInt(),
				Mockito.anyInt(),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.any(User.class)
			)
		).thenReturn(TMOCK);
		Mockito.when(cd.getAvailablePitches(
				Mockito.anyLong(),
				Mockito.any(Sport.class),
				Mockito.any(Instant.class),
				Mockito.any(Instant.class),
				Mockito.anyInt())).thenReturn(PITCHES);
		final Tournament t = ts.create(
				TNAME,
				SPORT,
				CLUB,
				MAX_TEAMS,
				TEAM_SIZE,
				STARTS,
				STARTS_AT,
				ENDS_AT,
				INSCR_ENDS,
				USER
		);
		Assert.assertEquals(TMOCK, t);
	}
	
	@Test
	public void createWithPastDate() throws Exception {
		try {
			ts.create(
					TNAME,
					SPORT,
					CLUB,
					MAX_TEAMS,
					TEAM_SIZE,
					STARTS,
					STARTS_AT,
					ENDS_AT,
					Instant.now().minus(2, ChronoUnit.DAYS),
					USER
			);
			Assert.assertTrue(false);
		} catch(Exception e) {
			Assert.assertEquals(InscriptionDateInPastException.class, e.getClass());
		}
	}

}
