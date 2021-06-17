package org.ggyool.toby.user.domain;

import org.ggyool.toby.user.domain.level.AbstractLevelDeterminer;
import org.ggyool.toby.user.domain.level.Level;
import org.ggyool.toby.user.domain.level.LevelDeterminer;

public class User {

    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public User() {
    }

    public User(String id, String name, String password) {
        this(id, name, password, Level.BASIC, 0, 0);
    }

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public boolean canUpgrade(LevelDeterminer levelDeterminer) {
        return ((AbstractLevelDeterminer) levelDeterminer).hasChangeable(this);
    }

    public void upgradeLevel(LevelDeterminer levelDeterminer) {
        level = levelDeterminer.determine(this);
    }

    public boolean hasSameLevel(Level level) {
        return this.level.isSame(level);
    }

    public int getLevelValue() {
        return level.getValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
