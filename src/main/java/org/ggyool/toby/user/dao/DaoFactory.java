package org.ggyool.toby.user.dao;

import org.ggyool.toby.datasource.H2DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DaoFactory {

    public UserDao createUserDao() {
        return new UserDao(new H2DataSource());
    }

    public JdbcUserDao createJdbcUserDao(final JdbcTemplate jdbcTemplate) {
        return new JdbcUserDao(jdbcTemplate);
    }
}
