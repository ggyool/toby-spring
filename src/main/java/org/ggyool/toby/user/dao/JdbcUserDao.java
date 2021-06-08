package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserDao {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    ;

    private RowMapper<User> rowMapper = (rs, rowNum) ->
        new User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password")
        );

    public JdbcUserDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public User get(String id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void add(User user) {
        final String sql = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
    }
}
