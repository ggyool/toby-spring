package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@DirtiesContext()
//@SpringBootTest(classes = {DaoFactory.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @DisplayName("유저 추가 및 조회")
    @Test
    void addAndGet() {
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(new User("ggyool", "뀰", "password"));
        assertThat(userDao.getCount()).isEqualTo(1);

        User foundUser = userDao.get("ggyool");
        assertThat(foundUser.getId()).isEqualTo("ggyool");
        assertThat(foundUser.getName()).isEqualTo("뀰");
        assertThat(foundUser.getPassword()).isEqualTo("password");
    }

    @DisplayName("없는 User를 조회하면 예외발생")
    @Test()
    void getFailBecauseNonexistentId() {
        assertThatThrownBy(() -> userDao.get("nonexistent"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("User가 몇 명 있는지 확인")
    @Test
    void getCount() {
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(new User("aaa", "에이", "apswd"));
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(new User("bbb", "비", "bpswd"));
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(new User("ccc", "씨", "cpswd"));
        assertThat(userDao.getCount()).isEqualTo(3);
    }
}
