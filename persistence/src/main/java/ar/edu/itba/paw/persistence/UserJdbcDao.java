package ar.edu.itba.paw.persistence;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;

@Repository
public class UserJdbcDao implements UserDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 10;
	
	private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) ->
	new User(rs.getLong("userId"), rs.getString("username"), rs.getString("password"),
			 Role.valueOf(rs.getString("role")), rs.getTimestamp("created_at"), rs.getTimestamp("deleted_at"));
	
	@Autowired
	public UserJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("users")
				.usingGeneratedKeyColumns("userid");
	}

	@Override
	public Optional<User> findById(final long id) {
		return jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", ROW_MAPPER, id)
				.stream()
				.findFirst();
	}
	
	@Override
	public Optional<User> findByUsername(final String username) {
		return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, username)
				.stream().findFirst();
	}
	
	@Override
	public User create(final String username, final String password, final Role role)
			throws UserAlreadyExistsException {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("username", username); // username == column name
		args.put("password", password);
		args.put("role", role);
		args.put("created_at", Timestamp.from(now));
		args.put("deleted_at", null);
		try {
			final Number userId = jdbcInsert.executeAndReturnKey(args);
			return new User(userId.longValue(), username, password, role, now, null);
		} catch(DuplicateKeyException e) {
			throw new UserAlreadyExistsException("User with the username " +
				username + " already exists");
		}
		
	}

}
