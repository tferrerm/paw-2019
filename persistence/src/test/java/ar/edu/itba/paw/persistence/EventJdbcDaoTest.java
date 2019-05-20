package ar.edu.itba.paw.persistence;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	private static final long EVENTID = 1;
	private static final long USERID = 1;
	private static final String USERNAME = "test@test.test";
	private static final String EVENTNAME = "test_event";
	private static final User OWNER = new User(USERID, USERNAME, "first", "last", "12345678", Role.ROLE_USER);
	private static final Club CLUB = new Club(1, "club", "location", Instant.now());
	private static final Pitch PITCH = new Pitch(1, CLUB, "pitch", Sport.TENNIS, Instant.now());
	private static final String DESCRIPTION = "description";
	private static final int MAX_PARTICIPANTS = 2;
	private static final Instant STARTS_AT = Instant.now();
	private static final Instant ENDS_AT = STARTS_AT.plus(1, ChronoUnit.HOURS);

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
		Assert.assertNotNull(event.getCreatedAt());
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
	}

	@Test
	public void testFindById() {
		final Optional<Event> event = ed.findByEventId(EVENTID);
		Assert.assertTrue(event.isPresent());
		Assert.assertEquals(EVENTID, event.get().getEventId());
	}

}