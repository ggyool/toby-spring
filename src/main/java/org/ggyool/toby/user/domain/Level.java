package org.ggyool.toby.user.domain;

import java.util.Arrays;

public enum Level {

    BASIC(1),
    SILVER(2),
    GOLD(3);

    int value;

    Level(int value) {
        this.value = value;
    }

    public static Level from(int value) {
        return Arrays.stream(values())
            .filter(level -> level.value == value)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 값을 가지는 Level은 없습니다."));
    }

    public int getValue() {
        return value;
    }
}
