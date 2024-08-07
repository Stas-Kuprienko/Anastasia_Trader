package com.anastasia.ui.service;

import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.user.Account;
import java.util.List;

public interface AccountService {

    Account createAccount(long userId, String clientId, String broker, String token);

    Account findByClientIdAndBroker(long userId, String clientId, Broker broker);

    List<Account> findByUserId(long userId);

    void deleteAccount(long userId, String clientId, Broker broker);
}
