package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.exception.ProfilePictureProcessingException;
import ar.edu.itba.paw.model.ProfilePicture;

public interface ProfilePictureService {
	
	public Optional<ProfilePicture> findByUserId(long userid);
	
	/**
	 * Handles a profile picture before its persistence
	 * @param userid Id of user the picture belongs to
	 * @param data Picture as raw data
	 * @throws IllegalArgumentException If the raw data is not identified as a picture
	 * @throws ProfilePictureProcessingException If there is an error when handling the picture's bytes
	 */
	public void create(long userid, byte[] data) throws ProfilePictureProcessingException;

}
