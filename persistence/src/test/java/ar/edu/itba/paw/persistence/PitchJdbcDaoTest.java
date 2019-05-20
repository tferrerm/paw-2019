package ar.edu.itba.paw.persistence;

import java.time.Instant;
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

import ar.edu.itba.paw.interfaces.PitchDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PitchJdbcDaoTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private PitchDao pd;

	private JdbcTemplate jdbcTemplate;
	
	private static final long PITCHID = 1;
	private static final long CLUBID = 1;
	private static final Club CLUB = new Club(CLUBID, "name", "location", Instant.now());
	private static final String NAME = "pitch";
	private static final String LOCATION = "location";
	private static final String CLUBNAME = "club";
	
	@Before
	public void setUp() {
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Rollback
	@Test
	public void testCreate() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "pitches");
		final Pitch pitch = pd.create(CLUB, NAME, Sport.TENNIS);
		Assert.assertNotNull(pitch);
		Assert.assertEquals(CLUBID, pitch.getClub().getClubid());
		Assert.assertEquals(NAME, pitch.getName());
		Assert.assertEquals(Sport.TENNIS, pitch.getSport());
		Assert.assertNotNull(pitch.getCreatedAt());
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "pitches"));
	}
	
	@Test
	public void testFindById() {
		final Optional<Pitch> pitch = pd.findById(PITCHID);
		Assert.assertTrue(pitch.isPresent());
		Assert.assertEquals(PITCHID, pitch.get().getPitchid());
		Assert.assertEquals(CLUBID, pitch.get().getClub().getClubid());
		Assert.assertEquals(NAME, pitch.get().getName());
		Assert.assertEquals(Sport.SOCCER, pitch.get().getSport());
		Assert.assertNotNull(pitch.get().getCreatedAt());
	}
	
	@Test
	public void testFindByClubId() {
		final List<Pitch> pitches = pd.findByClubId(CLUBID, 1);
		Assert.assertEquals(1, pitches.size());
		Assert.assertEquals(PITCHID, pitches.get(0).getPitchid());
		Assert.assertEquals(CLUBID, pitches.get(0).getClub().getClubid());
		Assert.assertEquals(NAME, pitches.get(0).getName());
		Assert.assertEquals(Sport.SOCCER, pitches.get(0).getSport());
		Assert.assertNotNull(pitches.get(0).getCreatedAt());
		
	}
	
	@Test
	public void testFindBy() {
		final List<Pitch> pitches = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(1, pitches.size());
		Assert.assertEquals(PITCHID, pitches.get(0).getPitchid());
		Assert.assertEquals(CLUBID, pitches.get(0).getClub().getClubid());
		Assert.assertEquals(NAME, pitches.get(0).getName());
		Assert.assertEquals(Sport.SOCCER, pitches.get(0).getSport());
		Assert.assertNotNull(pitches.get(0).getCreatedAt());
		final List<Pitch> noPitches = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.TENNIS.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(0, noPitches.size());
		final List<Pitch> pitches2 = pd.findBy(
				Optional.empty(),
				Optional.of(Sport.SOCCER.toString()),
				Optional.empty(),
				Optional.of(CLUBNAME),
				1);
		Assert.assertEquals(1, pitches2.size());
		final List<Pitch> invalidPage = pd.findBy(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME),
				2);
		Assert.assertEquals(0, invalidPage.size());
	}
	
	@Test
	public void testCountFiltered() {
		int count = pd.countFilteredPitches(
				Optional.of(NAME), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME));
		Assert.assertEquals(1, count);
		count = pd.countFilteredPitches(
				Optional.of(NAME), 
				Optional.empty(),
				Optional.empty(),
				Optional.of(CLUBNAME));
		Assert.assertEquals(1, count);
		count = pd.countFilteredPitches(
				Optional.of("BADSTRING"), 
				Optional.of(Sport.SOCCER.toString()),
				Optional.of(LOCATION),
				Optional.of(CLUBNAME));
		Assert.assertEquals(0, count);
	}
	
	@Test
	public void testCountPages() {
		Assert.assertEquals(1, pd.countPitchPages());
	}
	
	@Rollback
	@Test
	public void testDeletePitch() {
		pd.deletePitch(PITCHID);
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "pitches"));
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events_users"));
	}

}
