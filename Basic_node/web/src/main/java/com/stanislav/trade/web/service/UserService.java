package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.user.User;

import java.util.Optional;

public interface UserService {

    User create(String login, String password, String name);

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    boolean addTelegramChatId(User user, Long chatId);
}
