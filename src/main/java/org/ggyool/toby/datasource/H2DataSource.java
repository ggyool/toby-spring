package org.ggyool.toby.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

public class H2DataSource {

    private static final String H2_DRIVER_NAME = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:ee";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "";

    private final DataSource dataSource;

    public H2DataSource() {
        this.dataSource = DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();

        ((HikariDataSource) dataSource).setDriverClassName(H2_DRIVER_NAME);
        ((HikariDataSource) dataSource).setJdbcUrl(H2_URL);
        ((HikariDataSource) dataSource).setUsername(USER_NAME);
        ((HikariDataSource) dataSource).setPassword(PASSWORD);
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return dataSource.getConnection();
    }
}
