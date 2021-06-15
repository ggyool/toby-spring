package org.ggyool.toby.user.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import java.sql.SQLException;
import org.ggyool.toby.user.dao.resultsetstrategy.ResultSetStrategy;
import org.ggyool.toby.user.domain.User;
import org.ggyool.toby.user.exception.DuplicateUserIdException;

public class JdbcUserDao {

    private final JdbcContext jdbcContext;

    private final ResultSetStrategy<User> resultSetStrategy =
        (rs) -> new User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password")
        );


    public JdbcUserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public User get(String id) throws SQLException {
        final String sql = "SELECT * FROM USERS WHERE id = ?";
        return jdbcContext.executeSqlForObject(sql, resultSetStrategy, id);
    }

    public void add(User user) throws Throwable {
        final String sql = "INSERT INTO USERS(id, name, password) VALUES (?, ?, ?)";
        try {
            jdbcContext.executeSql(sql, user.getId(), user.getName(), user.getPassword());
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw new DuplicateUserIdException(user.getId())
                    .initCause(e);
            } else {
                throw e;
            }
        }
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM USERS");
    }
}
