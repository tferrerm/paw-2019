package ar.edu.itba.paw.service;

import org.junit.Assert;
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
	
	@Test
	public void invalidPictureTest() throws Exception {
		try {
			ps.convert(new byte[] {0x01, 0x05});
			Assert.assertTrue(false);
		} catch(Exception e) {
			Assert.assertEquals(IllegalArgumentException.class, e.getClass());
		}
	}

}
