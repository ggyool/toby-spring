package org.ggyool.toby.user.dao;

import java.util.List;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper = (rs, rowNum) -> new User(
        rs.getString("id"),
        rs.getString("name"),
        rs.getString("password")
    );

    public UserDaoJdbc() {
    }

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User get(String id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<User> getAllAsc() {
        final String sql = "SELECT * FROM USERS ORDER BY ID";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Integer getCount() {
        final String sql = "SELECT COUNT(*) FROM USERS";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void add(User user) {
        jdbcTemplate.update(
            "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)",
            user.getId(), user.getName(), user.getPassword()
        );
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM USERS");
    }

    public void deleteById(String id) {
        final String sql = "DELETE FROM USERS WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
