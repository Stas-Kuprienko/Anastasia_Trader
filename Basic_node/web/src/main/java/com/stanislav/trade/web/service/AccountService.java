package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;

import java.util.List;
import java.util.Set;

public interface AccountService {

    Account create(User user, String clientId, String token, String broker);

    Set<Account> findByUserId(long id);


}
