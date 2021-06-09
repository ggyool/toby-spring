package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

/*
별다른 설정 없이도 메서드 단위로 롤백이 된다.
JdbcTemplate을 사용할 때에만 그렇다는 생각을 하고 있음.
쌩 JDBC API를 사용한 UserDaoTest의 경우 롤백되지 않는다.
 */
@AutoConfigureTestDatabase(replace = Replace.NONE) // application.yml의 DB 타도록
@JdbcTest
class JdbcUserDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcUserDao jdbcUserDao;

    @BeforeEach
    void setUp() {
        DaoFactory daoFactory = new DaoFactory();
        jdbcUserDao = daoFactory.createJdbcUserDao(jdbcTemplate);
        jdbcUserDao.add(new User("existent", "존재", "password"));
    }

    @DisplayName("유저 조회")
    @Test
    void get() {
        // when
        User findUser = jdbcUserDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() {
        // given
        User user = new User("ggyool", "뀰", "password");
        jdbcUserDao.add(user);

        // when
        User findUser = jdbcUserDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }
}