package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.ggyool.toby.datasource.H2DataSource;
import org.ggyool.toby.datasource.MyDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(MyDataSource myDataSource) {
        return new UserDao(myDataSource);
    }

    @Bean
    public MyDataSource h2DataSource() {
        return new H2DataSource();
    }

    @Bean
    public JdbcUserDao jdbcUserDao(JdbcTemplate jdbcTemplate) {
        return new JdbcUserDao(jdbcTemplate);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
