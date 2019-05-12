package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Club;

@Component
public class ClubRowMapper implements RowMapper<Club> {

	@Override
	public Club mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Club(
				rs.getLong("clubid"),
				rs.getString("clubname"),
				rs.getString("location"),
				rs.getTimestamp("club_created_at").toInstant()
				);
	}

}
