package org.ggyool.toby.user.dao.resultsetstrategy;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetStrategy<T> {

    T makeObject(ResultSet resultSet) throws SQLException;
}
