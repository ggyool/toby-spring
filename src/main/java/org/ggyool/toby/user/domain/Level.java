package org.ggyool.toby.user.domain;

import java.util.Arrays;

public enum Level {
    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level level) {
        this.value = value;
        this.next = level;
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

    public Level getNext() {
        return next;
    }
}
