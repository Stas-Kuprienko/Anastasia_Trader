package org.stanislav.database;

import org.stanislav.entities.user.User;

public interface UserDAO extends DAO<User> {

    User create(String name, String login, String password);
}
