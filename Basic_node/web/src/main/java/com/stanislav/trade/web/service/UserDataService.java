package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDataService {

    User createUser(String login, String password, String name);

    Account createAccount(User user, String clientId, String token, String broker);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByLogin(String login);

    List<Account> getAccountsByLogin(String login);

    Optional<Account> findAccountByLoginClientBroker(String login, String clientId, Broker broker);

    void deleteAccount(String login, long accountId);

    boolean addTelegramChatId(User user, Long chatId);

    String decodeToken(String token) throws IllegalArgumentException;
}
