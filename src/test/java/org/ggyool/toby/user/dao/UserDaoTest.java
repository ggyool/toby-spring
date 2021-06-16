package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@DirtiesContext()
//@SpringBootTest(classes = {DaoFactory.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private User userA, userB, userC;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
        userA = new User("aaa", "에이", "apswd", Level.BASIC, 1, 0);
        userB = new User("bbb", "비", "bpswd", Level.SILVER, 55, 10);
        userC = new User("ccc", "씨", "cpswd", Level.GOLD, 100, 40);
    }

    @DisplayName("유저 추가 및 조회")
    @Test
    void addAndGet() {
        assertThat(userDao.getCount()).isEqualTo(0);

        userDao.add(userA);
        assertThat(userDao.getCount()).isEqualTo(1);
        checkSameValue(userDao.get(userA.getId()), userA);

        userDao.add(userB);
        assertThat(userDao.getCount()).isEqualTo(2);
        checkSameValue(userDao.get(userB.getId()), userB);

        userDao.add(userC);
        assertThat(userDao.getCount()).isEqualTo(3);
        checkSameValue(userDao.get(userC.getId()), userC);
    }

    @DisplayName("예외 - 중복 유저 추가")
    @Test
    void add_negativeCase_existentId() {
        // given
        userDao.add(userA);

        // when, then
        assertThatThrownBy(() -> userDao.add(new User("aaa", "새로운유저", "password")))
            .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("학습 테스트 - SQLExcception to DataAccessException")
    @Test
    void learningTest_translateSqlException() {
        userDao.add(userA);

        try {
            userDao.add(userA);
        } catch (DuplicateKeyException e) {
            SQLException se = (SQLException) e.getRootCause();
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException de = translator.translate(null, null, se);
            assertThat(de).isInstanceOf(DuplicateKeyException.class);
        }
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

        userDao.add(userA);
        assertThat(userDao.getCount()).isEqualTo(1);

        userDao.add(userB);
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(userC);
        assertThat(userDao.getCount()).isEqualTo(3);
    }

    @DisplayName("모든 유저 조회")
    @Test
    void getAllAsc() {
        // given
        userDao.add(userC);
        userDao.add(userA);
        userDao.add(userB);

        // when
        List<User> users = userDao.getAllAsc();

        // then - equals, hashCode 안 만들고 id만 비교하였음
        assertThat(users).extracting("id")
            .containsExactly(
                userA.getId(),
                userB.getId(),
                userC.getId()
            );
    }

    @DisplayName("데이터가 없는 경우 모든 유저 조회")
    @Test
    void getAllAsc_negativeCase_nonExistentData() {
        List<User> users = userDao.getAllAsc();
        assertThat(users).hasSize(0);
    }

    private void checkSameValue(User userA, User userB) {
        assertThat(userA).usingRecursiveComparison()
            .isEqualTo(userB);
    }
}
