package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.ggyool.toby.mydatasource.MyDataSource;
import org.ggyool.toby.mydatasource.MyH2DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(myH2DataSource());
    }

    @Bean
    public MyDataSource myH2DataSource() {
        return new MyH2DataSource();
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
