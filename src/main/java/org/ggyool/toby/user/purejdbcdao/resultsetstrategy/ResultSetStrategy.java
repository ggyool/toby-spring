package org.ggyool.toby.user.purejdbcdao.resultsetstrategy;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy<T> {

    T makeObject(ResultSet resultSet) throws SQLException;
}
