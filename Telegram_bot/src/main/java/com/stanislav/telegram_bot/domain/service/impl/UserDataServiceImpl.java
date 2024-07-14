package com.stanislav.telegram_bot.domain.service.impl;

import com.stanislav.telegram_bot.datasource.repositories.UserDao;
import com.stanislav.telegram_bot.domain.service.UserDataService;
import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("userDataService")
public class UserDataServiceImpl implements UserDataService {

    private final UserDao dao;


    @Autowired
    public UserDataServiceImpl(UserDao dao) {
        this.dao = dao;
    }


    @Override
    public User save(User user) {
        user = dao.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return dao.findByLogin(login);
    }

    @Override
    public boolean update(User user) {
        return false;
    }
}