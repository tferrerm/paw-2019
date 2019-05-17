package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class InscriptionsRowMapper implements RowMapper<Long[]> {
	
	private static final int EVENT_ID_INDEX = 1;
	private static final int EVENT_INSCRIPTIONS_INDEX = 2;
	
	/**
	 * Returns a combination of eventid and vacancies for that Event.
	 */
	@Override
	public Long[] mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Long[] { rs.getLong(EVENT_ID_INDEX), rs.getLong(EVENT_INSCRIPTIONS_INDEX) };
	}
	
}
