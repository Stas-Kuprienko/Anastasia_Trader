package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.jpa.AccountDao;
import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.TelegramChatId;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service("userDataService")
public class UserDataServiceRamImpl implements UserDataService {

    //TODO   TEMPORARY SOLUTION. NEED TO MAKE !!!!!!!!!!!

    private final ConcurrentHashMap<String, User> ram;
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;


    @Autowired
    public UserDataServiceRamImpl(UserDao userDao, AccountDao accountDao,
                                  PasswordEncoder passwordEncoder,
                                  JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
        this.ram = new ConcurrentHashMap<>();
    }


    @Override
    @Transactional
    public User createUser(String login, String password, String name)  {
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
    @Transactional
    public Account createAccount(User user, String clientId, String token, String broker) {
        Account account = new Account();
        account.setUser(user);
        account.setClientId(clientId);
        account.setToken(jwtBuilder.claim("token", token).compact());
        account.setBroker(Broker.valueOf(broker));
        account = accountDao.save(account);
        return account;
    }

    @Override
    public Optional<User> findUserById(Long id) {
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
    public Optional<User> findUserByLogin(String login) {
        var user = Optional.ofNullable(ram.get(login));
        if (user.isEmpty()) {
            user = userDao.findByLogin(login);
            user.ifPresent(u -> ram.put(u.getLogin(), u));
        }
        return user;
    }

    @Transactional
    @Override
    public Optional<Account> findAccountByLoginClientIdBroker(String login, String clientId, Broker broker) {
        try {
            User user = findUserByLogin(login).orElseThrow();
            return user.getAccounts()
                    .stream()
                    .filter(a -> a.getClientId().equals(clientId) && a.getBroker().equals(broker))
                    .findFirst();
        } catch (NoSuchElementException | NullPointerException e) {
            log.warn(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    @Transactional
    @Override
    public void deleteAccount(String login, long id) {
        var account = accountDao.findById(id);
        if (account.isPresent()) {
            if (account.get().getUser().getLogin().equals(login)) {
                accountDao.delete(account.get());
            }
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

    @Override
    public String decodeToken(String token) throws IllegalArgumentException {
        try {
            return (String) jwtParser
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("token");
        } catch (JwtException | ClassCastException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }
}