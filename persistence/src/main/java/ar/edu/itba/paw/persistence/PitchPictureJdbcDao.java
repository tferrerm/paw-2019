package ar.edu.itba.paw.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import ar.edu.itba.paw.interfaces.PitchPictureDao;
import ar.edu.itba.paw.model.PitchPicture;

public class PitchPictureJdbcDao implements PitchPictureDao {
	
	private JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;
	private static final int MAX_ROWS = 1;
	
	private static final RowMapper<PitchPicture> ROW_MAPPER = (rs, rowNum) ->
	new PitchPicture(rs.getLong("pitch_picture_id"), rs.getLong("pitchid"),
			rs.getBytes("data"));
	
	@Autowired
	public PitchPictureJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setMaxRows(MAX_ROWS);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("pitch_pictures")
				.usingGeneratedKeyColumns("pitch_picture_id");
	}

	@Override
	public Optional<PitchPicture> findByPitchId(long pitchid) {
		return jdbcTemplate.query("SELECT * FROM pitch_pictures WHERE pitchid = ?", ROW_MAPPER, pitchid)
				.stream()
				.findFirst();
	}

	@Override
	public void create(long pitchid, byte[] data) {
		final Map<String, Object> args = new HashMap<>();
		args.put("pitchid", pitchid);
		args.put("data", data);
		jdbcInsert.executeAndReturnKey(args);
	}

}
