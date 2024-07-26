/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.service.impl;

import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.exceptions.NotFoundException;
import com.stanislav.trade.web.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service("userService")
public class UserServiceTransactionAnnotatedImpl implements UserService {

    //TODO   TEMPORARY SOLUTION. NEED TO MAKE !!!!!!!!!!!

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceTransactionAnnotatedImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
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

    @Override
    public User findUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException("id=" + id));
    }

    @Override
    public User findUserByLogin(String login) {
        return userDao.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("login=" + login));
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        User user = userDao.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("login=" + login));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException();
        }
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