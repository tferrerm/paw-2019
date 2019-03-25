package ar.edu.itba.paw.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.model.ProfilePicture;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {
	
	@Autowired
	private ProfilePictureDao ppd;
	private static final String FORMAT = "png";
	private static final int MAX_WIDTH = 1000;
	private static final int MAX_HEIGHT = 1000;
	private static final int WIDTH = 0;
	private static final int HEIGHT = 1;

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		return ppd.findByUserId(userid);
	}

	@Override
	public void create(long userid, byte[] data) 
		throws IOException {
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
		if(img.getType() == BufferedImage.TYPE_CUSTOM) {
			img.flush();
			throw new IllegalArgumentException();
		}
		int[] dimensions = processImageSize(img.getWidth(), img.getHeight());
		BufferedImage newImg = Scalr.resize(
				img, Method.ULTRA_QUALITY, Mode.AUTOMATIC, dimensions[WIDTH],
				dimensions[HEIGHT], Scalr.OP_ANTIALIAS);
		ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
		ImageIO.write(newImg, FORMAT, convertedImage);
		img.flush();
		newImg.flush();
		ppd.create(userid, convertedImage.toByteArray());
	}
	
	private int[] processImageSize(int width, int height) {
		int[] dimensions = { width, height };
		if(width > MAX_WIDTH)
			dimensions[WIDTH] = MAX_WIDTH;
		if(height > MAX_HEIGHT)
			dimensions[HEIGHT] = MAX_HEIGHT;
		return dimensions;
	}

}
