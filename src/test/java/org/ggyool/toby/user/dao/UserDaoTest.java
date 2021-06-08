package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@JdbcTest
class UserDaoTest {

    @Autowired
    private DataSource dataSource;

    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new UserDao(dataSource);
        userDao.add(new User("existent", "존재", "password"));
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException {
        // when
        User findUser = userDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() throws SQLException {
        // given
        User user = new User("ggyool", "뀰", "password");
        userDao.add(user);

        // when
        User findUser = userDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }
}