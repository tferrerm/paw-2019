package ar.edu.itba.paw.interfaces;

import java.util.Optional;

import ar.edu.itba.paw.model.Inscription;

public interface InscriptionDao {
	
	public Optional<Inscription> findByIds(final long eventid, final long userid);
	
	public void deleteInscription(final long eventid, final long userid);
	
}
