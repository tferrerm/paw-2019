package ar.edu.itba.paw.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.model.ProfilePicture;

@Repository
public class ProfilePictureHibernateDao implements ProfilePictureDao {

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(long userid, byte[] data) {
		// TODO Auto-generated method stub

	}

}
