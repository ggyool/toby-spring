package org.ggyool.toby.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private User userA, userB, userC, userD, userE, userF, userG;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            userA = new User("a", "nameA", "pswdA", Level.BASIC, 49, 0),
            userB = new User("b", "nameB", "pswdB", Level.BASIC, 50, 0),
            userC = new User("c", "nameC", "pswdC", Level.SILVER, 60, 29),
            userD = new User("d", "nameD", "pswdD", Level.SILVER, 60, 30),
            userE = new User("e", "nameE", "pswdE", Level.GOLD, 100, 100),
            userF = new User("f", "nameF", "pswdF", Level.BASIC, 100, 100),
            userG = new User("g", "nameG", "pswdG", Level.GOLD, 60, 0)
        );
        userDao.deleteAll();
    }

    @DisplayName("유저를 추가한다 - 등급이 정해져 있지 않은 유저는 Basic 등급을 가진다.")
    @Test
    void add() {

    }

    @DisplayName("유저들의 등급 정보를 수정하여 반영한다.")
    @Test
    void upgradeLevels() {
        // given
        users.forEach(userDao::add);

        // when
        userService.upgradeLevels();

        // then
        checkLevel(userA, Level.BASIC);
        checkLevel(userB, Level.SILVER);
        checkLevel(userC, Level.SILVER);
        checkLevel(userD, Level.GOLD);
        checkLevel(userE, Level.GOLD);
        // TODO : remove
//        checkLevel(userF, Level.GOLD);
//        checkLevel(userG, Level.SILVER);
    }

    private void checkLevel(User user, Level level) {
        assertThat(userDao.get(user.getId())).extracting("level")
            .isEqualTo(level);
    }
}