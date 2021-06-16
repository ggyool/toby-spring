package org.ggyool.toby.user.purejdbcdao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PureJdbcDaoFactory.class})
class PureJdbcUserDaoTest {

    @Autowired
    private PureJdbcUserDao pureJdbcDao;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Throwable {
        pureJdbcDao.deleteAll();
        pureJdbcDao.add(new User("existent", "존재", "password"));
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException {
        // when
        User findUser = pureJdbcDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() throws Throwable {
        // given
        User user = new User("ggyool", "뀰", "password");
        pureJdbcDao.add(user);

        // when
        User findUser = pureJdbcDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }

    @DisplayName("예외 - 중복 유저 추가")
    @Test
    void add_negativeCase_existentId() {
        // when, then
        assertThatThrownBy(() -> pureJdbcDao.add(new User("existent", "새로운유저", "password")))
            .isInstanceOf(DuplicateKeyException.class);
    }
}
