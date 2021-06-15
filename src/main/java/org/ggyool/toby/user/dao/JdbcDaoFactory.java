package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class JdbcDaoFactory {

    @Bean
    public JdbcUserDao jdbcUserDao(JdbcContext jdbcContext) {
        return new JdbcUserDao(jdbcContext);
    }

    @Bean
    public JdbcContext jdbcContext(DataSource dataSource) {
        return new JdbcContext(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        final String MYSQL_URL = "jdbc:mysql://localhost:13306/toby_db?userSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
        final String USER_NAME = "root";
        final String PASSWORD = "root";

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(MYSQL_URL);
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
