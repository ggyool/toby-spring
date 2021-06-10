package org.ggyool.toby.mydatasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyH2DataSource implements MyDataSource {

    private static final String H2_DRIVER_NAME = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:testdb";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(H2_DRIVER_NAME);
        return DriverManager.getConnection(H2_URL, USER_NAME, PASSWORD);
    }
}
