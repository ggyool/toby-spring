package org.ggyool.toby.mydatasource;

import java.sql.Connection;
import java.sql.SQLException;

public interface MyDataSource {

    Connection getConnection() throws ClassNotFoundException, SQLException;
}
