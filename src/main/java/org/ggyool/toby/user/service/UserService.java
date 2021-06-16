package org.ggyool.toby.user.service;

import java.util.List;
import java.util.Objects;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;

public class UserService {

    private UserDao userDao;

    public UserService() {
    }

    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAllAsc();
        users.forEach(user -> {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        });
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level) {
            case BASIC:
                return user.getLogin() >= 50;
            case SILVER:
                return user.getRecommend() >= 30;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("알 수 없는 등급입니다. 등급 : " + level);
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
