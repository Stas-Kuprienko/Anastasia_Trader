package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("userService")
public class UserServiceRamImpl  implements UserService {

    //TODO   TEMPORARY SOLUTION. NEED TO REPLACE BY REDIS   !!!!!!!!!!!

    private final ConcurrentHashMap<Long, User> ram;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceRamImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.ram = new ConcurrentHashMap<>();
    }


    @Override
    public User create(String login, String password, String name)  {
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        ram.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = Optional.ofNullable(ram.get(id));
        if (user.isEmpty()) {
            user = userDao.findById(id);
        }
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        for (Map.Entry<?,User> e : ram.entrySet()) {
            if (e.getValue().getLogin().equals(login)) {
                return Optional.of(e.getValue());
            }
        }
        return userDao.findByLogin(login);
    }

    @Override
    public boolean addTelegramChatId(User user, Long chatId) {
        return userDao.addTelegramChatId(user, chatId);
    }
}