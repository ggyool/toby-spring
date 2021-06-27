package org.ggyool.toby.user.purejdbcdao;

import java.sql.SQLException;
import java.util.Objects;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.ggyool.toby.user.purejdbcdao.resultsetstrategy.ResultSetStrategy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class PureJdbcUserDao {

    private final JdbcContext jdbcContext;

    private final ResultSetStrategy<User> resultSetStrategy =
        (rs) -> new User(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getString("email"),
            Level.from(rs.getInt("level")),
            rs.getInt("login"),
            rs.getInt("recommend")
        );

    private final SQLExceptionTranslator sqlTranslator;

    public PureJdbcUserDao(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
        this.sqlTranslator = new SQLErrorCodeSQLExceptionTranslator(jdbcContext.getDataSource());
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
            DataAccessException dataAccessException = sqlTranslator.translate(null, null, e);
            if (Objects.isNull(dataAccessException)) {
                throw e;
            }
            throw dataAccessException;
        }
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("DELETE FROM USERS");
    }
}
