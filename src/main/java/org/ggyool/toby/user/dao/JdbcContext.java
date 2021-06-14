package org.ggyool.toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.ggyool.toby.user.dao.statementstrategy.StatementStrategy;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class JdbcContext {

    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(String sql) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(sql);
            }
        });
    }

    public void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = statementStrategy.makePreparedStatement(conn);
        ) {
            ps.executeUpdate();
        }
    }

    public void setDataSource(SimpleDriverDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
