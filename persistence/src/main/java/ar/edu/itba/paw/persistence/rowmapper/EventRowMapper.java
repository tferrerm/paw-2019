package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Event;

@Component
public class EventRowMapper implements RowMapper<Event> {
	
	@Autowired
	private UserRowMapper urm;

	@Override
	public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Event(
				rs.getLong("eventid"),
				rs.getString("name"),
				urm.mapRow(rs, rowNum),
				rs.getString("location"),
				rs.getString("description"),
				rs.getTimestamp("starts_at"),
				rs.getTimestamp("ends_at"),
				rs.getTimestamp("created_at"),
				rs.getTimestamp("deleted_at")
			);
	}

}
