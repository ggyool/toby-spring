package org.ggyool.toby.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.ggyool.toby.user.service.UserService.MIN_GOLD_RECOMMEND_COUNT;
import static org.ggyool.toby.user.service.UserService.MIN_SILVER_LOGIN_COUNT;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

//@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private MailSender mailSender;

    private User basicUser, silverUserSoon, silverUser, goldUserSoon, goldUser;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
            basicUser = new User("a", "nameA", "pswdA", "a@email.com", Level.BASIC, MIN_SILVER_LOGIN_COUNT - 1, 0),
            silverUserSoon = new User("b", "nameB", "pswdB", "b@email.com", Level.BASIC, MIN_SILVER_LOGIN_COUNT, 0),
            silverUser = new User("c", "nameC", "pswdC", "c@email.com", Level.SILVER, 60, MIN_GOLD_RECOMMEND_COUNT - 1),
            goldUserSoon = new User("d", "nameD", "pswdD", "d@email.com", Level.SILVER, 60, MIN_GOLD_RECOMMEND_COUNT),
            goldUser = new User("e", "nameE", "pswdE", "e@email.com", Level.GOLD, 100, 100)
        );
        userDao.deleteAll();
    }

    @DisplayName("유저를 추가한다 - 등급이 정해져 있지 않은 유저는 Basic 등급을 가진다.")
    @Test
    void add() {
        User nullLevelUser = new User("n", "nameN", "pswdN", "emailN", null, 0, 0);
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

    @DirtiesContext
    @DisplayName("유저들 등급 정보를 수정하여 반영한다.")
    @Test
    void upgradeLevels() throws SQLException {
        // given
        users.forEach(userDao::add);

        // 메일 발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 사용
        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        // when
        userService.upgradeLevels();

        // then
        checkLevelUpgraded(basicUser, false);
        checkLevelUpgraded(silverUserSoon, true);
        checkLevelUpgraded(silverUser, false);
        checkLevelUpgraded(goldUserSoon, true);
        checkLevelUpgraded(goldUser, false);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests).hasSize(2);
        assertThat(requests).containsExactly(
            silverUserSoon.getEmail(), goldUserSoon.getEmail()
        );
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getNextLevel());
        } else {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @DisplayName("중간에 한 명이라도 등급 업그레이드에 실패하면 모두 롤백되어야 한다.")
    @Test
    void upgradeLevels_fail_rollback() {
        // given
        userService = new FakeUserService(goldUserSoon.getId());
        userService.setUserDao(userDao);
        userService.setTransactionManager(transactionManager);
        userService.setMailSender(mailSender);
        users.forEach(userService::add);

        // when
        try {
            userService.upgradeLevels();
            fail("upgrade 동작 중 실패해야 합니다.");
        } catch (ArtificialUserServiceException e) {
        }

        // then
        users.forEach(
            user -> checkLevel(user, user.getLevel())
        );
    }

    private static class FakeUserService extends UserService {

        private final String exceptionalId;

        public FakeUserService(String exceptionalId) {
            this.exceptionalId = exceptionalId;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(exceptionalId)) {
                throw new ArtificialUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    private static class ArtificialUserServiceException extends RuntimeException {

    }

    private static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
        }
    }
}