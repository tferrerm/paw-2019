package ar.edu.itba.paw.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import ar.edu.itba.paw.interfaces.TournamentDao;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TournamentServiceImplTest {
	
	@InjectMocks
	private TournamentServiceImpl ts;
	
	@Mock
	private TournamentDao td;
	
	@Test
	public void createTournamentTest() {
		
	}

}
