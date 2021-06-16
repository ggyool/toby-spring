package org.ggyool.toby.user.service;

import java.util.List;
import org.ggyool.toby.user.dao.UserDao;
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

    // TODO : 개인적으로 모델을 설계하고 싶은 포인트라서 임시 브랜치 만들기 위해 커밋
    private void upgradeLevel(User user) {

    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
