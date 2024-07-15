/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.service;

import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class UserDataService {

    //TODO   TEMPORARY SOLUTION. NEED TO MAKE !!!!!!!!!!!

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserDataService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User createUser(String login, String password, String name) {
        User user = new User(
                login,
                passwordEncoder.encode(password),
                User.Role.USER,
                name != null ? name : "");
        userDao.save(user);
        return user;
    }

    public Optional<User> findUserById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findUserByLogin(String login) {
        return userDao.findByLogin(login);
    }

    public boolean addTelegramChatId(User user, Long chatId) {
        Optional<TelegramChatId> chat = userDao.findTelegramChatId(chatId);
        if (chat.isPresent() && chat.get().getUser().equals(user)) {
            return true;
        }
        return userDao.addTelegramChatId(user, chatId);
    }
}