package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao() {
    }

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User get(String id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
            ),
            id);
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
