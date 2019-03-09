package ar.edu.itba.paw.interfaces;

import java.io.IOException;
import java.util.Optional;

import ar.edu.itba.paw.model.ProfilePicture;

public interface ProfilePictureService {
	
	public Optional<ProfilePicture> findByUserId(long userid);
	
	public ProfilePicture create(long userid, byte[] data) throws IOException;

}
