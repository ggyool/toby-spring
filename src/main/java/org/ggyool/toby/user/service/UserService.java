package org.ggyool.toby.user.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.ggyool.toby.user.dao.UserDao;
import org.ggyool.toby.user.domain.Level;
import org.ggyool.toby.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class UserService {

    public static final int MIN_SILVER_LOGIN_COUNT = 50;
    public static final int MIN_GOLD_RECOMMEND_COUNT = 30;

    private UserDao userDao;
    private DataSource dataSource;

    public UserService() {
    }

    public void add(User user) {
        if (Objects.isNull(user.getLevel())) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() throws SQLException {
        // 트랜잭션 동기화 작업을 초기화한다.
        TransactionSynchronizationManager.initSynchronization();
        // DB 커넥션을 생성한다. 해당 유틸을 사용하면 Connection을 생성할 뿐만 아니라 트랜잭션 동기회에 사용하도록 저장소에 바인딩해줌.
        // 동기화가 되어있는 상태에서 JdbcTemplate을 사용하면 앞서 동기화시킨 Connection을 사용
        Connection con = DataSourceUtils.getConnection(dataSource);
        con.setAutoCommit(false);

        try {
            List<User> users = userDao.getAllAsc();
            users.forEach(user -> {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            });
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource); // Connection을 안전하게 닫는다.

            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
