package ar.edu.itba.paw.persistence.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Component
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User(rs.getLong("userId"),
						rs.getString("username"),
						rs.getString("firstname"),
						rs.getString("lastname"),
						rs.getString("password"),
						Role.valueOf(rs.getString("role")),
						rs.getTimestamp("created_at").toInstant()
						);
	}

}
