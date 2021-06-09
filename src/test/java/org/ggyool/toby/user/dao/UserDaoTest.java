package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.ggyool.toby.datasource.H2DataSource;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class UserDaoTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        DaoFactory daoFactory = new DaoFactory();
        userDao = daoFactory.createUserDao();
        userDao.add(new User("existent", "존재", "password"));
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException, ClassNotFoundException {
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