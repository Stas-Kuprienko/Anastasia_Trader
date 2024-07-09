package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface UserDataService {

    User createUser(String login, String password, String name);

    @Transactional
    Account createAccount(User user, String clientId, String token, String broker);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByLogin(String login);

    Optional<Account> findAccountByLoginClientIdBroker(String login, String clientId, Broker broker);

    void deleteAccount(String login, long id);

    boolean addTelegramChatId(User user, Long chatId);

    String decodeToken(String token) throws IllegalArgumentException;
}
