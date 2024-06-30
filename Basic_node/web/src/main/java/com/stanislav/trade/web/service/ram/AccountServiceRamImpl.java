package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service("accountService")
public class AccountServiceRamImpl implements AccountService {

    //TODO   TEMPORARY SOLUTION. NEED TO REPLACE BY REDIS   !!!!!!!!!!!

    private final ConcurrentHashMap<Long, Set<Account>> ram;
    private final AccountDao accountDao;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;
    private final TradingService tradingService;


    @Autowired
    public AccountServiceRamImpl(AccountDao accountDao, JwtBuilder jwtBuilder,
                                 JwtParser jwtParser, TradingService tradingService) {
        this.accountDao = accountDao;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
        this.tradingService = tradingService;
        this.ram = new ConcurrentHashMap<>();
    }


    @Override
    @Transactional
    public Account create(User user, String clientId, String token, String broker) {
        Account account = new Account();
        account.setUser(user);
        account.setClientId(clientId);
        account.setToken(jwtBuilder.claim("token", token).compact());
        account.setBroker(broker);
        //TODO get balance
        account.setBalance(BigDecimal.valueOf(0));
//        tradingService.getPortfolio(clientId);
        account = accountDao.save(account);
        account.setToken(token);
        if (ram.get(user.getId()) == null) {
            ram.put(user.getId(), Set.of(account));
        } else {
            ram.get(user.getId()).add(account);
        }
        return account;
    }

    @Override
    public Set<Account> findByUser(User user) {
        var accounts = ram.get(user.getId());
        if (accounts == null || accounts.isEmpty()) {
            accounts = new HashSet<>(accountDao.findAllByUser(user));
            ram.put(user.getId(), accounts);
        }
        return accounts;
    }

    @Override
    public Optional<Account> findById(User user, Long id) {
        List<Account> accounts = user.getAccounts();
        if (accounts == null || accounts.isEmpty()) {
            return Optional.empty();
        }
        return accounts.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    @Override
    public String decodeToken(Account account) throws ClassCastException, NullPointerException {
        return (String) jwtParser
                .parseSignedClaims(account.getToken())
                .getPayload()
                .get("token");
    }
}