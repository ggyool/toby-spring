package org.ggyool.toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.ggyool.toby.user.dao.resultsetstrategy.ResultSetStrategy;
import org.ggyool.toby.user.dao.statementstrategy.StatementStrategy;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class JdbcContext {

    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeSql(String sql, String... args) throws SQLException {
        workWithStatementStrategy(
            con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                fillArguments(ps, args);
                return ps;
            }
        );
    }

    public <T> T executeSqlForObject(String sql, ResultSetStrategy<T> resultSetStrategy, String... args)
        throws SQLException {

        StatementStrategy statementStrategy = (con) -> {
            PreparedStatement ps = con.prepareStatement(sql);
            fillArguments(ps, args);
            return ps;
        };
        return workWithStatementAndResultSetStrategy(statementStrategy, resultSetStrategy);
    }

    private void fillArguments(PreparedStatement ps, String[] args) throws SQLException {
        int i = 1;
        for (String arg : args) {
            ps.setString(i++, arg);
        }
    }

    private void workWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = statementStrategy.makePreparedStatement(conn);
        ) {
            ps.executeUpdate();
        }
    }

    private <T> T workWithStatementAndResultSetStrategy(StatementStrategy statementStrategy,
        ResultSetStrategy<T> resultSetStrategy) throws SQLException {

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = statementStrategy.makePreparedStatement(conn);
            ResultSet resultSet = ps.executeQuery();
        ) {
            resultSet.next();
            return resultSetStrategy.makeObject(resultSet);
        }
    }

    public void setDataSource(SimpleDriverDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
