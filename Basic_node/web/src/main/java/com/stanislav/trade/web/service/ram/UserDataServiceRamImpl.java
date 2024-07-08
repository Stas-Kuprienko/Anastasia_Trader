package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("userDataService")
public class UserDataServiceRamImpl implements UserDataService {

    //TODO   TEMPORARY SOLUTION. NEED TO REPLACE BY REDIS   !!!!!!!!!!!

    private final ConcurrentHashMap<String, User> ram;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserDataServiceRamImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.ram = new ConcurrentHashMap<>();
    }


    @Override
    @Transactional
    public User create(String login, String password, String name)  {
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        ram.put(user.getLogin(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        for (Map.Entry<?,User> e : ram.entrySet()) {
            if (e.getValue().getId().equals(id)) {
                return Optional.of(e.getValue());
            }
        }
        var user = userDao.findById(id);
        user.ifPresent(u -> ram.put(u.getLogin(), u));
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        var user = Optional.ofNullable(ram.get(login));
        if (user.isEmpty()) {
            user = userDao.findByLogin(login);
            user.ifPresent(u -> ram.put(u.getLogin(), u));
        }
        return user;
    }

    @Override
    public boolean addTelegramChatId(User user, Long chatId) {
        Optional<TelegramChatId> chat = userDao.findTelegramChatId(chatId);
        if (chat.isPresent() && chat.get().getUser().equals(user)) {
            return true;
        }
        return userDao.addTelegramChatId(user, chatId);
    }
}