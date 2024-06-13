package com.stanislav.telegram_bot.domain.service;

import com.stanislav.telegram_bot.entities.user.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    boolean update(User user);
}
