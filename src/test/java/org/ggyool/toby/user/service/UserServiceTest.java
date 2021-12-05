package org.ggyool.toby.user.service;

import org.ggyool.toby.factorybean.TxProxyFactoryBean;
import org.ggyool.toby.handler.TransactionHandler;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.ggyool.toby.user.service.UserServiceImpl.MIN_GOLD_RECOMMEND_COUNT;
import static org.ggyool.toby.user.service.UserServiceImpl.MIN_SILVER_LOGIN_COUNT;

//@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
class UserServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

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
        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        // when
        userService.upgradeLevels();

        // then
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated).hasSize(2);

        checkUserAndLevel(silverUserSoon, "b", Level.SILVER);
        checkUserAndLevel(goldUserSoon, "d", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests).hasSize(2);
        assertThat(requests).containsExactly(
                silverUserSoon.getEmail(), goldUserSoon.getEmail()
        );
    }

    private void checkUserAndLevel(User user, String expectedId, Level expectedLevel) {
        assertThat(user.getId()).isEqualTo(expectedId);
        assertThat(user.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User updatedUser = userDao.get(user.getId());
        if (upgraded) {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getNextLevel());
        } else {
            assertThat(updatedUser.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @DirtiesContext
    @DisplayName("중간에 한 명이라도 등급 업그레이드에 실패하면 모두 롤백되어야 한다.")
    @Test
    void upgradeLevels_fail_rollback() {
        // given

        FakeUserService fakeUserService = new FakeUserService(goldUserSoon.getId());
        fakeUserService.setUserDao(userDao);
        fakeUserService.setMailSender(mailSender);

        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setUserService(fakeUserService);
        users.forEach(userServiceTx::add);

        // when
        try {
            userServiceTx.upgradeLevels();
            fail("upgrade 동작 중 실패해야 합니다.");
        } catch (ArtificialUserServiceException e) {
        }

        // then
        users.forEach(
                user -> checkLevel(user, user.getLevel())
        );
    }

    @DirtiesContext
    @DisplayName("중간에 한 명이라도 등급 업그레이드에 실패하면 모두 롤백되어야 한다. (프록시 사용)")
    @Test
    void upgradeLevels_fail_rollback_with_proxy() {
        // given
        FakeUserService fakeUserService = new FakeUserService(goldUserSoon.getId());
        fakeUserService.setUserDao(userDao);
        fakeUserService.setMailSender(mailSender);

        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setPattern("upgradeLevels");
        transactionHandler.setTarget(fakeUserService);
        transactionHandler.setTransactionManager(transactionManager);

        UserService proxy = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[]{UserService.class}, transactionHandler
        );
        users.forEach(proxy::add);

        // when
        try {
            proxy.upgradeLevels();
            fail("upgrade 동작 중 실패해야 합니다.");
        } catch (ArtificialUserServiceException e) {
        }

        // then
        users.forEach(
                user -> checkLevel(user, user.getLevel())
        );
    }

    // TxProxyFactoryBean 으로 UserService 를 빈을 등록하였다.
    // 의존성이 주입되어서, userService 의 transactionHandler 가 가지고 있는 target 을 수정하기 어렵다.
    // 직접 빈을 생성하여 테스트한다.
    @Test
    @DirtiesContext
    void upgradeLevels_fail_rollback_with_factory_bean() throws Exception {
        // given
        FakeUserService fakeUserService = new FakeUserService(goldUserSoon.getId());
        fakeUserService.setUserDao(userDao);
        fakeUserService.setMailSender(mailSender);

        TxProxyFactoryBean txProxyFactoryBean = applicationContext.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(fakeUserService);
        UserService userService = (UserService) txProxyFactoryBean.getObject();

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

    private static class FakeUserService extends UserServiceImpl {

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

    private static class MockUserDao implements UserDao {

        private List<User> users; // 레벨 업그레이드 후보
        private List<User> updated = new ArrayList<>(); // 업그레이드 대상 오브젝트

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public User get(String id) {
            return null;
        }

        @Override
        public List<User> getAllAsc() {
            return this.users;
        }

        @Override
        public Integer getCount() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }
    }
}
