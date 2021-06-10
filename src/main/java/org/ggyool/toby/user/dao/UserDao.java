package org.ggyool.toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.ggyool.toby.mydatasource.MyDataSource;
import org.ggyool.toby.user.domain.User;

public class UserDao {

    private final MyDataSource dataSource;

    public UserDao(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User get(String id) throws SQLException, ClassNotFoundException {
        final String sql = "SELECT * FROM USERS WHERE id = ?";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
            rs.close();
            return user;
        }
    }

    public void add(User user) throws SQLException, ClassNotFoundException {
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
}
