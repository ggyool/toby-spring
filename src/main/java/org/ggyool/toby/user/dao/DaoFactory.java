package org.ggyool.toby.user.dao;

import javax.sql.DataSource;
import org.ggyool.toby.mydatasource.MyDataSource;
import org.ggyool.toby.mydatasource.MyDataSourceCounter;
import org.ggyool.toby.mydatasource.MyDockerMySqlDataSource;
import org.ggyool.toby.mydatasource.MyH2DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(dataSource());
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

    @Bean
    public MyDataSource myDataSourceCounter() {
        return new MyDataSourceCounter(myH2DataSource());
    }

    @Bean
    public MyDataSource myH2DataSource() {
        return new MyH2DataSource();
    }

    @Bean
    public MyDataSource myDockerMySqlDataSource() {
        return new MyDockerMySqlDataSource();
    }
}
