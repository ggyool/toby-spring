package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoXmlConfigTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        ApplicationContext applicationContext = new GenericXmlApplicationContext("applicationContext.xml");
        userDao = applicationContext.getBean("userDao", UserDao.class);
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException, ClassNotFoundException {
        // given
        userDao.add(new User("existent", "존재", "password"));

        // when
        User findUser = userDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() throws SQLException, ClassNotFoundException {
        // given
        User user = new User("ggyool", "뀰", "password");
        userDao.add(user);

        // when
        User findUser = userDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }
}
