package org.ggyool.toby.user.domain;

import java.util.Objects;

public class User {

    private String id;
    private String name;
    private String password;
    private String email;
    private Level level;
    private int login;
    private int recommend;

    public User() {
    }

    public User(String id, String name, String password, String email, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = getNextLevel();
        if (Objects.isNull(nextLevel)) {
            throw new IllegalStateException("더 이상 등급 업그레이드가 불가능합니다. 현재 등급 : " + level);
        }
        level = nextLevel;
    }

    public Level getNextLevel() {
        return level.getNext();
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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
