package org.ggyool.toby.user.dao.statementstrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.ggyool.toby.user.domain.User;

public class AddStrategy implements StatementStrategy {

    private final User user;

    public AddStrategy(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        return ps;
    }
}
