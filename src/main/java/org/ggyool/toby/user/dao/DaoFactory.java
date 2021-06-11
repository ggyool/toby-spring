package org.ggyool.toby.user.dao;

import org.ggyool.toby.mydatasource.MyDataSource;
import org.ggyool.toby.mydatasource.MyDataSourceCounter;
import org.ggyool.toby.mydatasource.MyDockerMySqlDataSource;
import org.ggyool.toby.mydatasource.MyH2DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(myDataSourceMaker());
    }

    @Bean
    public MyDataSource myDataSourceMaker() {
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
