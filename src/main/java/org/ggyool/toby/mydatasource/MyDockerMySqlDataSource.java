package org.ggyool.toby.mydatasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDockerMySqlDataSource implements MyDataSource {

    private static final String MYSQL_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:13306/toby_db?userSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER_NAME);
        return DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);
    }
}
