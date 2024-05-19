package com.stanislav.datasource;

import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;

import java.util.List;

public interface AccountDao extends EntityDao<Account, Long> {

    List<Account> getByUser(User user);
}