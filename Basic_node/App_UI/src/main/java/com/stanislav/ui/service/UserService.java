package com.stanislav.ui.service;

import com.stanislav.ui.model.user.User;

public interface UserService {
    User registry(String login, String password, String name);

    User logIn(String login, String password);

    User findById(Long id);

    User findByLogin(String login);
}
