package ar.edu.itba.paw.persistence;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserHibernateDaoTest {

	@Autowired
	private UserDao userDao;

	private static final long USERID = 1;
	private static final String USERNAME = "user@name.com";
	private static final String FIRSTNAME = "first";
	private static final String LASTNAME = "last";
	private static final String PASSWORD = "12345678";

	@Transactional
	@Rollback
	@Test
	public void testCreate() throws UserAlreadyExistsException {
		final String username = "cool-user@domain.com";
		final User user = userDao.create(username, FIRSTNAME, LASTNAME, PASSWORD, Role.ROLE_USER);
		Assert.assertNotNull(user);
		Assert.assertEquals(username, user.getUsername());
		Assert.assertEquals(FIRSTNAME, user.getFirstname());
		Assert.assertEquals(LASTNAME, user.getLastname());
		Assert.assertEquals(PASSWORD, user.getPassword());
		Assert.assertEquals(Role.ROLE_USER, user.getRole());
		Assert.assertNotNull(user.getCreatedAt());
	}

	@Test
	public void testFindById() {
		final Optional<User> user = userDao.findById(USERID);
		Assert.assertTrue(user.isPresent());
		Assert.assertEquals(USERID, user.get().getUserid());
		Assert.assertEquals(USERNAME, user.get().getUsername());
		Assert.assertEquals(FIRSTNAME, user.get().getFirstname());
		Assert.assertEquals(LASTNAME, user.get().getLastname());
		Assert.assertEquals(PASSWORD, user.get().getPassword());
	}

	@Test
	public void testFindByUsername() {
		final Optional<User> user = userDao.findByUsername(USERNAME);
		Assert.assertTrue(user.isPresent());
		Assert.assertEquals(USERNAME, user.get().getUsername());
		Assert.assertEquals(FIRSTNAME, user.get().getFirstname());
		Assert.assertEquals(LASTNAME, user.get().getLastname());
		Assert.assertEquals(PASSWORD, user.get().getPassword());
	}

}
