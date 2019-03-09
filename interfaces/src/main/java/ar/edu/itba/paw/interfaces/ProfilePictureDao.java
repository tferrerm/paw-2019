package ar.edu.itba.paw.interfaces;

import java.util.Optional;
import ar.edu.itba.paw.model.ProfilePicture;

public interface ProfilePictureDao {
	
	public Optional<ProfilePicture> findByUserId(long userid);
	
	public ProfilePicture create(long userid, byte[] data);

}
