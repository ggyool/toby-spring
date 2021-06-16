package org.ggyool.toby.user.domain.level;

import org.ggyool.toby.user.domain.User;

public interface LevelDeterminer {

    Level determine(User user);
}
