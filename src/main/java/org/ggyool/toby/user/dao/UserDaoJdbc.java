package org.ggyool.toby.user.dao;

import java.util.List;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.ggyool.toby.user.domain.level.Level;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper = (rs, rowNum) -> new User(
        rs.getString("id"),
        rs.getString("name"),
        rs.getString("password"),
        Level.from(rs.getInt("level")),
        rs.getInt("login"),
        rs.getInt("recommend")
    );

    public UserDaoJdbc() {
    }

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User get(String id) {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<User> getAllAsc() {
        final String sql = "SELECT * FROM USERS ORDER BY ID";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Integer getCount() {
        final String sql = "SELECT COUNT(*) FROM USERS";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(
            "INSERT INTO USERS(id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)",
            user.getId(), user.getName(), user.getPassword(), user.getLevelValue(), user.getLogin(), user.getRecommend()
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM USERS");
    }

    @Override
    public void deleteById(String id) {
        final String sql = "DELETE FROM USERS WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void update(User user) {
        final String sql = "UPDATE USERS SET name = ?, password = ?, level = ?, login = ?, recommend = ? WHERE id = ?";
        jdbcTemplate.update(
            sql,
            user.getName(), user.getPassword(), user.getLevelValue(), user.getLogin(), user.getRecommend(), user.getId()
        );
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
