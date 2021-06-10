package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcDaoFactory {

    @Bean
    public JdbcUserDao jdbcUserDao(JdbcTemplate jdbcTemplate) {
        return new JdbcUserDao(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
