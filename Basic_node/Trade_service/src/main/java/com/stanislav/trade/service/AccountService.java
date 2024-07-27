package com.stanislav.trade.service;

import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.exception.AccessDeniedException;

import java.util.List;

public interface AccountService {

    Account createAccount(User user, String clientId, String token, String broker);

    List<Account> findByUserId(long userId);

    List<Account> findByLogin(String login);

    Account findByClientIdAndBroker(long userId, String clientId, Broker broker) throws AccessDeniedException;

    void deleteAccount(String login, long accountId);

    String decodeToken(String token) throws IllegalArgumentException;
}
