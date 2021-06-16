package org.ggyool.toby.user.service;

import java.util.List;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;

public class UserService {

    private UserDao userDao;

    public UserService() {
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAllAsc();
        users.forEach(user -> {
            upgradeLevel(user);
            userDao.update(user);
        });
    }

    private void upgradeLevel(User user) {
        boolean isChanged = false;
        if (user.isBasicLevel() && user.getLogin() >= 50) {
            user.setLevel(Level.SILVER);
            isChanged = true;
        } else if (user.isSilverLevel() && user.getRecommend() >= 30) {
            user.setLevel(Level.GOLD);
            isChanged = true;
        }
        if (isChanged) {
            userDao.update(user);
        }
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
