package ar.edu.itba.paw.persistence;

import java.util.Optional;

import ar.edu.itba.paw.interfaces.TournamentDao;
import ar.edu.itba.paw.model.Tournament;

public class TournamentHibernateDao implements TournamentDao {
	
	public Optional<Tournament> findById(long tournamentid) {
		return Optional.empty();
	}

}
