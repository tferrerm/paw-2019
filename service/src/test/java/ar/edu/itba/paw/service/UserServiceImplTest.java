package ar.edu.itba.paw.service;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.TestConfig;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceImplTest {
	
	@Mock
	private UserDao ud;
	
	@InjectMocks
	private UserServiceImpl us;
	
	private static final String EMAIL = "email@email.email";
	private static final String FIRSTNAME = "first";
	private static final String LASTNAME = "last";
	private static final String PASSWORD = "12345678";
	private static final Instant NOW = Instant.now();
	private static final User USER = new User(EMAIL, FIRSTNAME, LASTNAME, PASSWORD, Role.ROLE_USER,
			NOW);
	
	@Test
	public void findByIdExistingTest() {
		Mockito.when(ud.findById(Mockito.anyLong())).thenReturn(Optional.of(USER));
		final Optional<User> op = us.findById(1);
		Assert.assertTrue(op.isPresent());
		Assert.assertEquals(EMAIL, op.get().getUsername());
		Assert.assertEquals(FIRSTNAME, op.get().getFirstname());
		Assert.assertEquals(LASTNAME, op.get().getLastname());
		Assert.assertEquals(PASSWORD, op.get().getPassword());
		Assert.assertEquals(Role.ROLE_USER, op.get().getRole());
		Assert.assertEquals(NOW, op.get().getCreatedAt());
	}
	
	@Test
	public void findByNonExistentIdTest() {
		Mockito.when(ud.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		final Optional<User> op = us.findById(1);
		Assert.assertFalse(op.isPresent());
	}

}
