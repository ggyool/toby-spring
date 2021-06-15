package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import org.ggyool.toby.user.domain.User;
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
    void setUp() throws SQLException {
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
    void add() throws SQLException {
        // given
        User user = new User("ggyool", "뀰", "password");
        jdbcUserDao.add(user);

        // when
        User findUser = jdbcUserDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }
}
