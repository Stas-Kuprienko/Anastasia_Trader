package com.anastasia.ui.service;

import com.anastasia.ui.model.user.User;

public interface UserService {

    User registry(String login, String password, String name);

    User logIn(String login, String password);

    User findById(Long id);

    User findByLogin(String login);
}
