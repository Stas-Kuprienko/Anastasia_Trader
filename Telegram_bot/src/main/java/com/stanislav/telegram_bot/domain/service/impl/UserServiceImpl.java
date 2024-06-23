package com.stanislav.telegram_bot.domain.service.impl;

import com.stanislav.telegram_bot.datasource.cache.UserCache;
import com.stanislav.telegram_bot.datasource.repositories.UserDao;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserDao dao;
    private final UserCache cache;


    @Autowired
    public UserServiceImpl(UserDao dao, UserCache cache) {
        this.dao = dao;
        this.cache = cache;
    }


    @Override
    public User save(User user) {
        user = dao.save(user);
        cache.put(user.getChatId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.ofNullable(cache.get(id));
        if (user.isEmpty()) {
            user = dao.findById(id);
            user.ifPresent(u -> cache.put(id, u));
        }
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        var user = cache.get(login);
        if (user.isEmpty()) {
            user = dao.findByLogin(login);
            user.ifPresent(u -> cache.put(u.getChatId(), u));
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        return false;
    }
}