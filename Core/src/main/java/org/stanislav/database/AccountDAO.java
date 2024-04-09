package org.stanislav.database;

import org.stanislav.entities.user.Account;
import org.stanislav.entities.user.User;

import java.util.List;

public interface AccountDAO extends DAO<Account> {

    List<Account> getByUser(User user);

    Account create(String clientId, User user, String broker, String token);
}