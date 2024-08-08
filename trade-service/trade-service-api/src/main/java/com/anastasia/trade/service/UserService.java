package com.anastasia.trade.service;

import com.anastasia.trade.entities.user.User;

public interface UserService {

    User createUser(String login, String password, String name);

    User findUserById(Long id);

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, String password);

    boolean addTelegramChatId(User user, Long chatId);
}
