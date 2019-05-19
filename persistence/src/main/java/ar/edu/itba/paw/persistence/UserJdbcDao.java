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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.exception.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.rowmapper.UserRowMapper;

@Repository
public class UserJdbcDao implements UserDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 10;
	
	@Autowired
	private UserRowMapper urm;
	
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
		return jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", urm, id)
				.stream()
				.findFirst();
	}
	
	@Override
	public Optional<User> findByUsername(final String username) {
		return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", urm, username)
				.stream().findFirst();
	}
	
	@Override
	public Optional<Integer> countVotesReceived(final long userid) {
		return jdbcTemplate.query("SELECT sum(vote) "
				+ " FROM events_users WHERE eventid IN "
				+ " (SELECT eventid FROM events WHERE userid = ?)", (rs, rowNum) -> rs.getInt("s"), userid)
				.stream().findFirst();
	}
	
	@Override
	public User create(final String username, final String firstname, final String lastname,
			final String password, final Role role) throws UserAlreadyExistsException {
		final Map<String, Object> args = new HashMap<>();
		Instant now = Instant.now();
		args.put("username", username); // username == column name
		args.put("firstname", firstname);
		args.put("lastname", lastname);
		args.put("password", password);
		args.put("role", role);
		args.put("created_at", Timestamp.from(now));
		try {
			final Number userId = jdbcInsert.executeAndReturnKey(args);
			return new User(userId.longValue(), username, firstname, lastname,
					password, role, now);
		} catch(DuplicateKeyException e) {
			throw new UserAlreadyExistsException("User with the username " +
				username + " already exists");
		}
		
	}

}
