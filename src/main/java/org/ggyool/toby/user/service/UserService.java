package org.ggyool.toby.user.service;

import org.ggyool.toby.user.domain.User;

public interface UserService {

    void add(User user);

    void upgradeLevels();
}
