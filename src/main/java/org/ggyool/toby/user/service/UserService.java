package org.ggyool.toby.user.service;

import java.util.List;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.User;
import org.ggyool.toby.user.domain.level.BasicLevelDeterminer;

public class UserService {

    private UserDao userDao;

    public UserService() {
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAllAsc();
        users.forEach(user -> {
            user.upgradeLevel(new BasicLevelDeterminer());
            userDao.update(user);
        });
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
