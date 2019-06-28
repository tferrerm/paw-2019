package ar.edu.itba.paw.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PictureServiceImplTest {
	
	@InjectMocks
	private PictureServiceImpl ps;
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidPictureTest() throws Exception {
		ps.convert(new byte[] {0x01, 0x05});
	}

}
