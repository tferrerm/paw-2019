package ar.edu.itba.paw.persistence;

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

import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.model.Club;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ClubHibernateDaoTest {
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private ClubDao cd;

	private JdbcTemplate jdbcTemplate;
	
	private static final long CLUBID = 1;
	private static final String LOCATION = "location";
	private static final String CLUBNAME = "club";
	
	@Before
	public void setUp() {
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Rollback
	@Test
	public void testCreate() {
		final Club club = cd.create(CLUBNAME, LOCATION);
		Assert.assertNotNull(club);
		Assert.assertEquals(CLUBNAME, club.getName());
		Assert.assertEquals(LOCATION, club.getLocation());
		Assert.assertNotNull(club.getCreatedAt());
		Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "clubs"));
	}
	
	@Test
	public void testFindById() {
		final Optional<Club> club = cd.findById(CLUBID);
		Assert.assertTrue(club.isPresent());
		Assert.assertEquals(CLUBID, club.get().getClubid());
		Assert.assertEquals(CLUBNAME, club.get().getName());
		Assert.assertNotNull(club.get().getCreatedAt());
	}
	
	@Test
	public void findAllTest() {
		final List<Club> clubs = cd.findAll(1);
		Assert.assertEquals(1, clubs.size());
		Assert.assertEquals(CLUBID, clubs.get(0).getClubid());
		Assert.assertEquals(CLUBNAME, clubs.get(0).getName());
		Assert.assertEquals(LOCATION, clubs.get(0).getLocation());
		Assert.assertNotNull(clubs.get(0).getCreatedAt());
		final List<Club> noClubs = cd.findAll(2);
		Assert.assertEquals(0, noClubs.size());
	}
	
	@Test
	public void testFindBy() {
		final List<Club> clubs = cd.findBy(
				Optional.of(CLUBNAME),
				Optional.of(LOCATION),
				1);
		Assert.assertEquals(1, clubs.size());
		Assert.assertEquals(CLUBID, clubs.get(0).getClubid());
		Assert.assertEquals(CLUBNAME, clubs.get(0).getName());
		Assert.assertEquals(LOCATION, clubs.get(0).getLocation());
		Assert.assertNotNull(clubs.get(0).getCreatedAt());
		final List<Club> noClubs = cd.findBy(
				Optional.of("BADSTRING"),
				Optional.of(LOCATION),
				1);
		Assert.assertEquals(0, noClubs.size());
		final List<Club> clubs2 = cd.findBy(
				Optional.of(CLUBNAME),
				Optional.empty(),
				1);
		Assert.assertEquals(1, clubs2.size());
		final List<Club> invalidPage = cd.findBy(
				Optional.of(CLUBNAME),
				Optional.of(LOCATION),
				2);
		Assert.assertEquals(0, invalidPage.size());
	}
	
	@Test
	public void testCountPages() {
		int count = cd.countClubPages();
		Assert.assertEquals(1, count);
	}
	
	@Test
	public void testCountPastEvents() {
		int count = cd.countPastEvents(CLUBID);
		Assert.assertEquals(1, count);
	}
	
	@Test
	public void testCountFiltered() {
		int count = cd.countFilteredClubs(Optional.of(CLUBNAME), Optional.of(LOCATION));
		Assert.assertEquals(1, count);
		count = cd.countFilteredClubs(Optional.empty(), Optional.of(LOCATION));
		Assert.assertEquals(1, count);
		count = cd.countFilteredClubs(Optional.of("BADSTRING"), Optional.of(LOCATION));
		Assert.assertEquals(0, count);
	}
	
	@Rollback
	@Test
	public void testDeletePitch() {
		cd.deleteClub(CLUBID);
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "clubs"));
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "pitches"));
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
		Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events_users"));
	}

}
