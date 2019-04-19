package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import ar.edu.itba.paw.model.Pitch;

public class PitchRowMapper implements RowMapper<Pitch> {
	
	@Autowired
	private ClubRowMapper crm;

	@Override
	public Pitch mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Pitch(
				rs.getLong("pitchid"),
				crm.mapRow(rs, rowNum),
				rs.getString("name"),
				rs.getString("sport"),
				rs.getTimestamp("created_at").toInstant()
				);
	}

}
