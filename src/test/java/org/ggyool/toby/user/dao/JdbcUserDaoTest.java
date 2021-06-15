package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.ggyool.toby.user.domain.User;
import org.ggyool.toby.user.exception.DuplicateUserIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JdbcDaoFactory.class})
class JdbcUserDaoTest {

    @Autowired
    private JdbcUserDao jdbcUserDao;

    @BeforeEach
    void setUp() throws Throwable {
        jdbcUserDao.deleteAll();
        jdbcUserDao.add(new User("existent", "존재", "password"));
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException {
        // when
        User findUser = jdbcUserDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() throws Throwable {
        // given
        User user = new User("ggyool", "뀰", "password");
        jdbcUserDao.add(user);

        // when
        User findUser = jdbcUserDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }

    @DisplayName("예외 - 중복 유저 추가")
    @Test
    void add_negativeCase_existentId() {
        // when, then
        assertThatThrownBy(() -> jdbcUserDao.add(new User("existent", "새로운유저", "password")))
            .isInstanceOf(DuplicateUserIdException.class);
    }

    @Test
    void test() throws Throwable {
        jdbcUserDao.add(new User("existent", "새로운유저", "password"));
    }
}
