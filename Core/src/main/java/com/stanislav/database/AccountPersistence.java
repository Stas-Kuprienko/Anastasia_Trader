package com.stanislav.database;

import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;

import java.util.List;

public interface AccountPersistence extends EntityPersistence<Account> {

    List<Account> getByUser(User user);
}