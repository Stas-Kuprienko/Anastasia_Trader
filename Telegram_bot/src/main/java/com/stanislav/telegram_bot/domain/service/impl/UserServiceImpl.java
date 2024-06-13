package com.stanislav.telegram_bot.domain.service.impl;

import com.stanislav.telegram_bot.datasource.repositories.UserDao;
import com.stanislav.telegram_bot.domain.service.UserService;
import com.stanislav.telegram_bot.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final HashMap<Long, User> cache;  //TODO temporary

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
        this.cache = new HashMap<>();
    }


    @Override
    public User save(User user) {
        user = userDao.save(user);
        cache.put(user.getChatId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.ofNullable(cache.get(id));
        if (user.isEmpty()) {
            user = userDao.findById(id);
        }
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<User> user = cache.values().stream().filter(u -> u.getLogin().equals(login)).findFirst();
        if (user.isEmpty()) {
            user = userDao.findByLogin(login);
        }
        return user;
    }

    @Override
    public boolean update(User user) {
        return false;
    }
}
