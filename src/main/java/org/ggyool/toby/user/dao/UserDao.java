package org.ggyool.toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

    private DataSource dataSource;

    public UserDao() {
    }

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User get(String id) throws SQLException {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        ResultSet rs = null;

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (!rs.next()) {
                throw new EmptyResultDataAccessException("유저를 조회하는데 실패했습니다.", 1);
            }
            User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
            return user;
        } finally {
            rs.close();
        }
    }

    public int getCount() throws SQLException {
        final String sql = "SELECT COUNT(*) FROM USERS";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            return count;
        }
    }

    public void add(User user) throws SQLException {
        final String sql = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        }
    }

    public void deleteAll() throws SQLException {
        final String sql = "DELETE FROM USERS";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.executeUpdate();
        }
    }

    public void deleteById(String id) throws SQLException {
        final String sql = "DELETE FROM USERS WHERE id = ?";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
