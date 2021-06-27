package org.ggyool.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
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
public class UserDaoXmlConfigTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @DisplayName("유저 조회")
    @Test
    void get() throws SQLException, ClassNotFoundException {
        // given
        userDao.add(new User("existent", "존재", "password", "existent@email.com", Level.BASIC, 0, 0));

        // when
        User findUser = userDao.get("existent");

        // then
        assertThat(findUser.getId()).isEqualTo("existent");
    }

    @DisplayName("유저 추가")
    @Test
    void add() throws SQLException, ClassNotFoundException {
        // given
        User user = new User("ggyool", "뀰", "password", "ggyool@email.com", Level.BASIC, 0, 0);
        userDao.add(user);

        // when
        User findUser = userDao.get(user.getId());

        // then
        assertThat(findUser.getId()).isEqualTo("ggyool");
    }
}
