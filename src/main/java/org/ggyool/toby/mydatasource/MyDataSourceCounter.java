package org.ggyool.toby.mydatasource;

import java.sql.Connection;
import java.sql.SQLException;

public class MyDataSourceCounter implements MyDataSource {

    private MyDataSource dataSource;
    private int count = 0;

    public MyDataSourceCounter() {
    }

    public MyDataSourceCounter(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        count++;
        System.out.println("count: " + count);
        return dataSource.getConnection();
    }

    public void setDataSource(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
