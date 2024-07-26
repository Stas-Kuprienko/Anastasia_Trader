package com.stanislav.ui.service;

import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.model.user.Broker;
import com.stanislav.ui.model.user.User;

import java.util.List;

public interface AccountService {

    Account createAccount(User user, String clientId, String token, String broker);

    Account findByClientIdAndBroker(long userId, String clientId, Broker broker);

    List<Account> findByLogin(String userLogin);

    void deleteAccount(String userLogin, Long accountId);

    String decodeToken(String token);
}
