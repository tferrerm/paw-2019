package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Club;

@Component
public class ClubRowMapper implements RowMapper<Club> {
	
	@Autowired
	private UserRowMapper urm;

	@Override
	public Club mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Club(
				rs.getLong("clubid"),
				urm.mapRow(rs, rowNum),
				rs.getString("name"),
				rs.getString("location"),
				rs.getTimestamp("created_at").toInstant()
				);
	}

}
