package org.ggyool.toby.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("userId", "userName", "pswd", "email", null, 0, 0);
    }

    @DisplayName("유저의 등급 업그레이드에 성공")
    @Test
    void upgradeLevel() {
        Arrays.stream(Level.values())
            .filter(level -> Objects.nonNull(level.getNext()))
            .forEach(
                level -> {
                    user.setLevel(level);
                    user.upgradeLevel();
                    assertThat(user.getLevel()).isEqualTo(level.getNext());
                }
            );
    }

    @DisplayName("유저의 등급 업그레이드에 실패")
    @Test
    void upgradeLevel_fail() {
        Arrays.stream(Level.values())
            .filter(level -> Objects.isNull(level.getNext()))
            .forEach(
                level -> {
                    user.setLevel(level);
                    assertThatThrownBy(() -> user.upgradeLevel())
                        .isInstanceOf(IllegalStateException.class);
                }
            );
    }
}
