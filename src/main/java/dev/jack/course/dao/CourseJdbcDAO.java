package dev.jack.course.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

import dev.jack.course.model.*;

@Component
public class CourseJdbcDAO implements DAO<Course> {

	private static final Logger log = LoggerFactory.getLogger(CourseJdbcDAO.class);
	private JdbcTemplate jdbcTemplate;
	
	RowMapper<Course> rowMapper = (rs, rowNum) -> {
		Course course = new Course();
		course.setCourseId(rs.getInt("course_id"));
		course.setTitle(rs.getString("title"));
		course.setDescription(rs.getString("description"));
		course.setLink(rs.getString("link"));
		return course;
	};
	public CourseJdbcDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Course> list() {
		String sql = "SELECT course_id, title, description, link FROM course";
		
		/* using lambda: jdbcTemplate.query(String, RowMapper(ResultSet rs, int rowNum))
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Course course = new Course();
			course.setCourseId(rs.getInt("course_id"));
			course.setTitle(rs.getString("title"));
			course.setDescription(rs.getString("description"));
			course.setLink(rs.getString("link"));
			return course;
		});
		*/

		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public void create(Course t) {
		String sql = "insert into course(course_id,title,description,link) values (?,?,?,?)";
		try {
			int insert = jdbcTemplate.update(sql, t.getCourseId(),t.getTitle(), t.getDescription(), t.getLink());
			if (insert >= 1) {
				log.info("New course created: " + t.getTitle());
			} else {
				log.info("Cannot insert: " + t.getTitle());
			}
		} catch (Exception e) {
			log.info("Insert failed: " + t.getCourseId()+", "+t.getTitle());
			e.printStackTrace();
		}
	}

	@Override
	public Optional<Course> get(int id) {
		String sql = "select course_id,title,description,link from course where course_id = ?";
		Course course = null;
		try {
			course = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
		} catch (DataAccessException e) {
			log.info("Course not found: " + id);
		} catch (Exception e) {
			log.info(sql + " Failed. "+id);
			e.printStackTrace();
		}
		
		return Optional.ofNullable(course);
	}

	@Override
	public void update(Course t, int id) {
		String sql = "update course set title = ?, description = ?, link = ? where course_id = ?";
		int update = jdbcTemplate.update(sql, t.getTitle(),t.getDescription(),t.getLink(),id);
		if (update == 1) {
			log.info("Course updated: " + t.getTitle());
		}
	}

	@Override
	public void delete(int id) {
		int delete = jdbcTemplate.update("delete from course where course_id = ?", id);
		log.info("Deleted row(s): " + delete);
	}
}
