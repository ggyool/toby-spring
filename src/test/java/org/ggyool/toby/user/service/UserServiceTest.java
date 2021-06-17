package org.ggyool.toby.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ggyool.toby.user.service.UserService.MIN_GOLD_RECOMMEND_COUNT;
import static org.ggyool.toby.user.service.UserService.MIN_SILVER_LOGIN_COUNT;

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

    private User basicUser, silverUserSoon, silverUser, goldUserSoon, goldUser, doubleLevelUpUser, levelDownUser;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            basicUser = new User("a", "nameA", "pswdA", Level.BASIC, MIN_SILVER_LOGIN_COUNT - 1, 0),
            silverUserSoon = new User("b", "nameB", "pswdB", Level.BASIC, MIN_SILVER_LOGIN_COUNT, 0),
            silverUser = new User("c", "nameC", "pswdC", Level.SILVER, 60, MIN_GOLD_RECOMMEND_COUNT - 1),
            goldUserSoon = new User("d", "nameD", "pswdD", Level.SILVER, 60, MIN_GOLD_RECOMMEND_COUNT),
            goldUser = new User("e", "nameE", "pswdE", Level.GOLD, 100, 100),
            doubleLevelUpUser = new User("f", "nameF", "pswdF", Level.BASIC, 100, 100),
            levelDownUser = new User("g", "nameG", "pswdG", Level.GOLD, 60, 0)
        );
        userDao.deleteAll();
    }

    @DisplayName("유저를 추가한다 - 등급이 정해져 있지 않은 유저는 Basic 등급을 가진다.")
    @Test
    void add() {
        User nullLevelUser = new User("n", "nameN", "pswdN", null, 0, 0);
        userService.add(nullLevelUser);
        checkLevel(nullLevelUser, Level.BASIC);

        userService.add(basicUser);
        checkLevel(basicUser, basicUser.getLevel());

        userService.add(silverUser);
        checkLevel(silverUser, silverUser.getLevel());

        userService.add(goldUser);
        checkLevel(goldUser, goldUser.getLevel());
    }

    private void checkLevel(User user, Level level) {
        assertThat(userDao.get(user.getId())).extracting("level")
            .isEqualTo(level);
    }

    @DisplayName("유저들 등급 정보를 수정하여 반영한다.")
    @Test
    void upgradeLevels() {
        // given
        users.forEach(userDao::add);

        // when
        userService.upgradeLevels();

        // then
        checkLevelUpgraded(basicUser, false);
        checkLevelUpgraded(silverUserSoon, true);
        checkLevelUpgraded(silverUser, false);
        checkLevelUpgraded(goldUserSoon, true);
        checkLevelUpgraded(goldUser, false);
        // TODO : 책에서는 2단 승급이나, 등급을 떨어뜨리는 상황을 고려하지 않음
//        checkLevel(doubleLevelUpUser, Level.GOLD);
//        checkLevel(levelDownUser, Level.SILVER);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getNextLevel());
        } else {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getLevel());
        }
    }
}