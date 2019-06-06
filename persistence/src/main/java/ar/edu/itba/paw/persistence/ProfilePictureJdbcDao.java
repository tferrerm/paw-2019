package ar.edu.itba.paw.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import ar.edu.itba.paw.interfaces.ProfilePictureDao;
import ar.edu.itba.paw.model.ProfilePicture;

public class ProfilePictureJdbcDao implements ProfilePictureDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 1;
	
	//private static final RowMapper<ProfilePicture> ROW_MAPPER = (rs, rowNum) ->
	//new ProfilePicture(rs.getLong("profile_picture_id"), rs.getLong("userid"),
	//		rs.getBytes("data"));
	
	@Autowired
	public ProfilePictureJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("profile_pictures")
				.usingGeneratedKeyColumns("profile_picture_id");
	}

	@Override
	public Optional<ProfilePicture> findByUserId(long userid) {
		return null;//jdbcTemplate.query("SELECT * FROM profile_pictures WHERE userid = ?", ROW_MAPPER, userid)
				//.stream()
				//.findFirst();
	}

	@Override
	public void create(long userid, byte[] data) {
		final Map<String, Object> args = new HashMap<>();
		args.put("userid", userid);
		args.put("data", data);
		jdbcInsert.executeAndReturnKey(args);
	}

}
