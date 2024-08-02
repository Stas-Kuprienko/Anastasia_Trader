/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.service.impl;

import com.stanislav.trade.datasource.jpa.AccountDao;
import com.stanislav.trade.datasource.jpa.UserDao;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.exception.AccessDeniedException;
import com.stanislav.trade.exception.NotFoundException;
import com.stanislav.trade.service.AccountService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service("accountService")
public class AccountServiceTransactionalAnnotatedImpl implements AccountService {

    private final AccountDao accountDao;
    private final UserDao userDao;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    @Autowired
    public AccountServiceTransactionalAnnotatedImpl(AccountDao accountDao, UserDao userDao, JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
    }


    @Override
    @Transactional
    public Account createAccount(long userId, String clientId, String token, String broker) {
        User user = userDao.findById(userId).orElseThrow(() -> new NotFoundException("user id=" + userId));
        Account account = new Account();
        account.setUser(user);
        account.setClientId(clientId);
        account.setToken(jwtBuilder.claim("token", token).compact());
        account.setBroker(Broker.valueOf(broker));
        account = accountDao.save(account);
        return account;
    }

    @Override
    @Transactional
    public List<Account> findByUserId(long userId) {
        User user = userDao.findById(userId).orElseThrow(
                () -> new NotFoundException("userId=" + userId));
        return accountDao.findAllByUser(user);
    }

    @Override
    @Transactional
    public List<Account> findByLogin(String login) {
        Optional<User> user = userDao.findByLogin(login);
        return accountDao.findAllByUser(
                user.orElseThrow(() -> new NotFoundException("login=" + login)));
    }

    @Override
    @Transactional
    public Account findByClientIdAndBroker(long userId, String clientId, Broker broker) throws AccessDeniedException {
        Map<String, Object> params = HashMap.newHashMap(2);
        params.put("clientId", clientId);
        params.put("broker", broker);
        List<Account> accounts = accountDao.findByParameters(params);
        NotFoundException notFoundException = new NotFoundException(
                "userId=" + userId + ", clientId=" + clientId + ", broker=" + broker);
        if (accounts.isEmpty()) {
            log.info(notFoundException.getMessage());
            throw notFoundException;
        }
        Optional<Account> account = Optional.of(accounts.getFirst());
        if (account.get().getUser().getId() == userId) {
            return account.orElseThrow(() -> notFoundException);
        } else {
            log.warn("user=" + userId
                    + " tries access to account=" + broker + ':' + clientId);
            throw new AccessDeniedException(
                    "parameters: {login: '%s', clientId: '%s', broker: '%s'}".formatted(userId, clientId, broker));
        }
    }

    @Override
    @Transactional
    public void deleteAccount(String login, long accountId) {
        var account = accountDao.findById(accountId);
        if (account.isPresent()) {
            if (account.get().getUser().getLogin().equals(login)) {
                accountDao.delete(account.get());
            }
        }
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
