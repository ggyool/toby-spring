package org.ggyool.toby.user.service;

import java.util.List;
import java.util.Objects;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class UserServiceImpl implements UserService {

    public static final int MIN_SILVER_LOGIN_COUNT = 50;
    public static final int MIN_GOLD_RECOMMEND_COUNT = 30;

    private UserDao userDao;
    private MailSender mailSender;

    public UserServiceImpl() {
    }

    @Override
    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    @Override
    public void upgradeLevels() {
        List<User> users = userDao.getAllAsc();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        switch (level) {
            case BASIC:
                return user.getLogin() >= MIN_SILVER_LOGIN_COUNT;
            case SILVER:
                return user.getRecommend() >= MIN_GOLD_RECOMMEND_COUNT;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("알 수 없는 등급입니다. 등급 : " + level);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + " 로 업그레이드되었습니다.");

        mailSender.send(mailMessage);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
