package org.ggyool.toby.user.service;

import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.level.LevelDeterminer;

public class UserService {

    private UserDao userDao;
    private LevelDeterminer levelDeterminer;

    public UserService() {
    }

    public void upgradeLevels() {
        userDao.getAllAsc()
            .stream()
            .filter(user -> user.canUpgrade(levelDeterminer))
            .forEach(user -> {
                user.upgradeLevel(levelDeterminer);
                userDao.update(user);
            });
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setLevelDeterminer(LevelDeterminer levelDeterminer) {
        this.levelDeterminer = levelDeterminer;
    }
}
