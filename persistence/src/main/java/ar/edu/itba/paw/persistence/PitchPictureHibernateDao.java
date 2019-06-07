package ar.edu.itba.paw.persistence;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.PitchPictureDao;
import ar.edu.itba.paw.model.PitchPicture;

public class PitchPictureHibernateDao implements PitchPictureDao {

	@Override
	public Optional<PitchPicture> findByPitchId(long pitchid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(long pitchid, byte[] data) {
		// TODO Auto-generated method stub

	}

}
