package org.ggyool.toby.user.domain.level;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import org.ggyool.toby.user.domain.User;

public class BasicLevelDeterminer extends AbstractLevelDeterminer {

    private static final Map<BiPredicate<Integer, Integer>, Level> CHECKER;

    static {
        CHECKER = new HashMap<>();
        CHECKER.put((login, recommend) -> login < 50, Level.BASIC);
        CHECKER.put((login, recommend) -> 50 <= login && recommend < 30, Level.SILVER);
        CHECKER.put((login, recommend) -> 50 <= login && recommend >= 30, Level.GOLD);
    }

    @Override
    public Level determine(User user) {
        return CHECKER.entrySet()
            .stream()
            .filter(entry -> entry.getKey().test(user.getLogin(), user.getRecommend()))
            .map(Entry::getValue)
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("등급을 찾는데 실패하였습니다."));
    }
}
