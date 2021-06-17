package org.ggyool.toby.user.domain.level;

import org.ggyool.toby.user.domain.User;

public abstract class AbstractLevelDeterminer implements LevelDeterminer {

    public boolean hasChangeable(User user) {
        return !user.hasSameLevel(determine(user));
    }

    public abstract Level determine(User user);
}
