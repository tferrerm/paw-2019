package ar.edu.itba.paw.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.ProfilePictureProcessingException;
import ar.edu.itba.paw.interfaces.PictureService;
import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.interfaces.ProfilePictureService;
import ar.edu.itba.paw.model.ProfilePicture;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {
	
	@Autowired
	private ProfilePictureDao ppd;
	
	@Autowired
	private PictureService ps;
	
	public static final int MAX_WIDTH = 300;
	public static final int MAX_HEIGHT = 300;

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		if(userid <= 0) {
			throw new IllegalArgumentException("Id must be greater than zero.");
		}
		return ppd.findByUserId(userid);
	}

	@Transactional
	@Override
	public void create(long userid, byte[] data) 
		throws ProfilePictureProcessingException {
		if(data.length == 0)
			return;
		try {
			byte[] convertedPicture = ps.convert(data, MAX_WIDTH, MAX_HEIGHT);
			ppd.create(userid, convertedPicture);
		} catch(IllegalArgumentException | IOException e) {
			throw new ProfilePictureProcessingException("The profile picture could not be processed. " + 
					e.getMessage());
		}
	}

}
