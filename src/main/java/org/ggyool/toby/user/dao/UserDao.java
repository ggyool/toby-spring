package org.ggyool.toby.user.dao;

import java.util.List;
import org.ggyool.toby.user.domain.User;

public interface UserDao {

    User get(String id);

    List<User> getAllAsc();

    Integer getCount();

    void add(User user);

    void deleteAll();

    void deleteById(String id);
}
