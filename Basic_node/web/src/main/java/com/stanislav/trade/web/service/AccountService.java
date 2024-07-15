/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.service;

import com.stanislav.trade.datasource.jpa.AccountDao;
import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {

    private final AccountDao accountDao;
    private final UserDao userDao;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    @Autowired
    public AccountService(AccountDao accountDao, UserDao userDao, JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
    }


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


    @Transactional
    public List<Account> getAccountsByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user=" + login + " is not found");
        }
        return accountDao.findAllByUser(user.get());
    }


    @Transactional
    public Optional<Account> findAccountByLoginClientBroker(String login, String clientId, Broker broker) throws AccessDeniedException {
        Map<String, Object> params = HashMap.newHashMap(2);
        params.put("clientId", clientId);
        params.put("broker", broker);
        List<Account> accounts = accountDao.findByParameters(params);
        if (accounts.isEmpty()) {
            return Optional.empty();
        }
        Optional<Account> account = Optional.of(accounts.getFirst());
        if (account.get().getUser().getLogin().equals(login)) {
            return account;
        } else {
            log.warn("parameters: {login: '%s', clientId: '%s', broker: '%s'}".formatted(login, clientId, broker));
            throw new AccessDeniedException("user=" + login
                    + " tries access to account=" + broker + ':' + clientId);
        }
    }

    @Transactional
    public void deleteAccount(String login, long accountId) {
        var account = accountDao.findById(accountId);
        if (account.isPresent()) {
            if (account.get().getUser().getLogin().equals(login)) {
                accountDao.delete(account.get());
            }
        }
    }

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
