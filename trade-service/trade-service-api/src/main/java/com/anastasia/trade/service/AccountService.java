package com.anastasia.trade.service;

import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.user.Account;
import com.anastasia.trade.exception.AccessDeniedException;

import java.util.List;

public interface AccountService {

    Account createAccount(long userId, String clientId, String token, String broker);

    List<Account> findByUserId(long userId);

    List<Account> findByLogin(String login);

    Account findByClientIdAndBroker(long userId, String clientId, Broker broker) throws AccessDeniedException;

    void deleteAccount(long userId, long accountId);

    void deleteByClientIdAndBroker(long userId, String clientId, Broker broker);

    String decodeToken(String token) throws IllegalArgumentException;
}
