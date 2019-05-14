package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Pitch;
import ar.edu.itba.paw.model.Sport;

@Component
public class SimplePitchRowMapper implements RowMapper<Pitch> {
	
	@Override
	public Pitch mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Pitch(
				rs.getLong("pitchid"),
				rs.getString("pitchname"),
				Sport.valueOf(rs.getString("sport")),
				rs.getTimestamp("pitch_created_at").toInstant()
				);
	}

}
