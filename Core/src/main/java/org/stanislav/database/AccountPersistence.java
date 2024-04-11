package org.stanislav.database;

import org.stanislav.entities.user.Account;
import org.stanislav.entities.user.User;

import java.util.List;

public interface AccountPersistence extends EntityPersistence<Account> {

    List<Account> getByUser(User user);
}