package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = Replace.NONE)

@Transactional // JdbcTemplate 사용해서 먹힌다고 추측
@EnableAutoConfiguration // application.yml DataSource 등록하도록
@SpringBootTest(classes = {JdbcDaoFactory.class})
class JdbcUserDaoTest {

    @Autowired
    private JdbcUserDao jdbcUserDao;

    @BeforeEach
    void setUp() {
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
