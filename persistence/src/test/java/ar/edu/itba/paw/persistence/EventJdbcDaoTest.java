package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.UserAlreadyJoinedException;
import ar.edu.itba.paw.interfaces.EventDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.Sport;
import ar.edu.itba.paw.model.User;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EventJdbcDaoTest {

	@Autowired
	private DataSource ds;

	@Autowired
	private EventDao ed;

	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	private static final long OLD_EVENTID = 1;
	private static final String OLD_EVENTNAME = "old_event";
	private static final long EVENTID = 2;
	private static final long USERID = 1;
	private static final String USERNAME = "test@test.test";
	private static final String EVENTNAME = "event";
	private static final User OWNER = new User(USERNAME, "first", "last", "12345678", Role.ROLE_USER, Instant.now());
	private static final long CLUBID = 1;
	private static final Club CLUB = new Club(1, "club", "location", Instant.now());
	private static final Pitch PITCH = new Pitch(1, CLUB, "pitch", Sport.TENNIS, Instant.now());
	private static final String DESCRIPTION = "description";
	private static final int MAX_PARTICIPANTS = 2;
	private static final Instant STARTS_AT = Instant.now();
	private static final int DURATION = 1;
	private static final Instant ENDS_AT = STARTS_AT.plus(DURATION, ChronoUnit.HOURS);
	private static final Event EVENT = new Event(EVENTNAME, OWNER, PITCH, DESCRIPTION, 2, STARTS_AT, ENDS_AT);
	private static final Timestamp STARTS = Timestamp.valueOf("2030-05-20 10:00:00");
	private static final Timestamp ENDS = Timestamp.valueOf("2030-05-20 11:00:00");

	@Rollback
	@Test
	public void testCreateEvent() throws Exception {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "events");
		final Event event = ed.create(EVENTNAME, OWNER, PITCH, DESCRIPTION, 
				MAX_PARTICIPANTS, STARTS_AT, ENDS_AT);
		Assert.assertNotNull(event);
		Assert.assertEquals(EVENTNAME, event.getName());
		Assert.assertEquals(PITCH, event.getPitch());
		Assert.assertEquals(DESCRIPTION, event.getDescription());
		Assert.assertEquals(MAX_PARTICIPANTS, event.getMaxParticipants());
		Assert.assertEquals(STARTS_AT, event.getStartsAt());
		Assert.assertEquals(ENDS_AT, event.getEndsAt());
		Assert.assertNotNull(event.getCreatedAt());
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
	}

	@Test
	public void testFindById() {
		final Optional<Event> event = ed.findByEventId(EVENTID);
		Assert.assertTrue(event.isPresent());
		Assert.assertEquals(EVENTNAME, event.get().getName());
		Assert.assertEquals(PITCH.getPitchid(), event.get().getPitch().getPitchid());
		Assert.assertEquals(DESCRIPTION, event.get().getDescription());
		Assert.assertEquals(MAX_PARTICIPANTS, event.get().getMaxParticipants());
		Assert.assertEquals(STARTS.toInstant(), event.get().getStartsAt());
		Assert.assertEquals(ENDS.toInstant(), event.get().getEndsAt());
		Assert.assertNotNull(event.get().getCreatedAt());
	}
	
	@Test
	public void testFindByOwner() {
		List<Event> events = ed.findByOwner(true, OWNER.getUserid(), 1);
		Assert.assertEquals(1, events.size());
		List<Event> pastEvents = ed.findByOwner(false, OWNER.getUserid(), 1);
		Assert.assertEquals(1, pastEvents.size());
	}
	
	@Test
	public void testFindFuture() {
		List<Event> events = ed.findFutureEvents(1);
		Assert.assertEquals(1, events.size());
		Assert.assertEquals(EVENTNAME, events.get(0).getName());
		Assert.assertEquals(PITCH.getPitchid(), events.get(0).getPitch().getPitchid());
		Assert.assertEquals(DESCRIPTION, events.get(0).getDescription());
		Assert.assertEquals(MAX_PARTICIPANTS, events.get(0).getMaxParticipants());
		Assert.assertEquals(STARTS.toInstant(), events.get(0).getStartsAt());
		Assert.assertEquals(ENDS.toInstant(), events.get(0).getEndsAt());
		Assert.assertNotNull(events.get(0).getCreatedAt());
	}
	
	@Test
	public void testFindEventUsers() {
		List<User> users = ed.findEventUsers(EVENTID, 1);
		Assert.assertEquals(2, users.size());
		List<User> noUsers = ed.findEventUsers(EVENTID, 2);
		Assert.assertEquals(0, noUsers.size());
	}
	
	@Test
	public void testFindCurrentEventsInPitch() {
		List<Event> events = ed.findCurrentEventsInPitch(PITCH.getPitchid());
		Assert.assertEquals(0, events.size()); // Inserted events are either in the past or years from now
	}
	
	@Test
	public void testFindBy() {
		List<Event> events = ed.findBy(
				true,
				Optional.of(EVENTNAME),
				Optional.of(CLUB.getName()),
				Optional.of(Sport.SOCCER.toString()),
				Optional.empty(),
				Optional.of(1),
				Optional.empty(),
				1);
		Assert.assertEquals(1, events.size());
		Assert.assertEquals(EVENTID, events.get(0).getEventId());
		Assert.assertEquals(EVENTNAME, events.get(0).getName());
		List<Event> includingOldEvents = ed.findBy(
				false,
				Optional.of(EVENTNAME), // Name: event, search: '%' || 'event' || '%'
				Optional.of(CLUB.getName()),
				Optional.of(Sport.SOCCER.toString()),
				Optional.empty(),
				Optional.of(1),
				Optional.empty(),
				1);
		Assert.assertEquals(2, includingOldEvents.size());
		List<Event> oldEvents = ed.findBy(
				false,
				Optional.of(OLD_EVENTNAME), // Name: old_event
				Optional.of(CLUB.getName()),
				Optional.of(Sport.SOCCER.toString()),
				Optional.empty(),
				Optional.of(1),
				Optional.empty(),
				1);
		Assert.assertEquals(1, oldEvents.size());
		Assert.assertEquals(OLD_EVENTID, oldEvents.get(0).getEventId());
		Assert.assertEquals(OLD_EVENTNAME, oldEvents.get(0).getName());
	}
	
	@Test
	public void testCountFilteredEvents() {
		int count = ed.countFilteredEvents(
				true,
				Optional.of(EVENTNAME),
				Optional.of(CLUB.getName()),
				Optional.empty(),
				Optional.empty(),
				Optional.of(1),
				Optional.empty());
		Assert.assertEquals(1, count);
		count = ed.countFilteredEvents(
				false,
				Optional.of(EVENTNAME),
				Optional.of(CLUB.getName()),
				Optional.empty(),
				Optional.empty(),
				Optional.of(1),
				Optional.empty());
		Assert.assertEquals(2, count);
		count = ed.countFilteredEvents(
				false,
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.of(Instant.now()));
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testCountFutureEventPages() {
		Assert.assertEquals(1, ed.countFutureEventPages());
	}
	
	@Test
	public void testCountParticipants() {
		Assert.assertEquals(2, ed.countParticipants(EVENTID));
	}
	
	@Rollback
	@Test
	public void testJoinEvent() throws Exception {
		try {
			ed.joinEvent(OWNER, EVENT);
			Assert.assertTrue(false);
		} catch(Exception e) {
			Assert.assertEquals(UserAlreadyJoinedException.class, e.getClass());
		}
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "events_users");
		ed.joinEvent(OWNER, EVENT);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events_users"));
	}
	
	@Rollback
	@Test
	public void testLeaveEvent() {
		ed.leaveEvent(OWNER, EVENT);
		Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events_users"));
	}
	
	@Rollback
	@Test
	public void testKickFromEvent() {
		ed.kickFromEvent(2, 2);
		Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events_users"));
	}
	
	@Test
	public void testCountUserEvents() {
		int count = ed.countUserEvents(false, OWNER.getUserid());
		Assert.assertEquals(1, count);
		count = ed.countUserEvents(true, OWNER.getUserid());
		Assert.assertEquals(1, count);
	}
	
	@Test
	public void testCountUserCurrentOwnedEvents() {
		Assert.assertEquals(1, ed.countUserOwnedCurrEvents(OWNER.getUserid()));
	}
	
	@Test
	public void testFavoriteSport() {
		Optional<Sport> fav = ed.getFavoriteSport(OWNER.getUserid());
		Assert.assertTrue(fav.isPresent());
		Assert.assertEquals(Sport.SOCCER, fav.get());
	}
	
	@Test
	public void testFavoriteClub() {
		Optional<Club> club = ed.getFavoriteClub(OWNER.getUserid());
		Assert.assertTrue(club.isPresent());
		Assert.assertEquals(CLUBID, club.get().getClubid());
	}
	
	@Rollback
	@Test
	public void testDeleteEvent() {
		ed.deleteEvent(EVENTID);
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
	}
	
	@Test
	public void testVoteBalance() {
		Optional<Integer> balance = ed.getVoteBalance(OLD_EVENTID);
		Assert.assertTrue(balance.isPresent());
		Assert.assertEquals(0, balance.get().intValue());
	}
	
	@Test
	public void testGetUserVote() {
		Optional<Integer> vote = ed.getUserVote(OLD_EVENTID, OWNER.getUserid());
		Assert.assertTrue(vote.isPresent());
		Assert.assertEquals(-1, vote.get().intValue());
	}
	
	@Rollback
	@Test
	public void testVote() {
		int result = ed.vote(true, EVENTID, OWNER.getUserid());
		Assert.assertEquals(1, result);
		result = ed.vote(false, -1, OWNER.getUserid());
		Assert.assertEquals(0, result);
		result = ed.vote(true, -1, -1);
		Assert.assertEquals(0, result);
	}

}