package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service("accountService")
public class AccountServiceRamImpl implements AccountService {

    //TODO   TEMPORARY SOLUTION. NEED TO REPLACE BY REDIS   !!!!!!!!!!!

    private final ConcurrentHashMap<Long, Set<Account>> ram;
    private final AccountDao accountDao;


    @Autowired
    public AccountServiceRamImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
        this.ram = new ConcurrentHashMap<>();
    }


    @Override
    public Account create(User user, String clientId, String token, String broker) {
        Account account = new Account();
        account.setUser(user);
        account.setClientId(clientId);
        account.setToken(token);
        account.setBroker(broker);
        account = accountDao.save(account);
        if (ram.get(user.getId()) == null) {
            ram.put(user.getId(), Set.of(account));
        } else {
            ram.get(user.getId()).add(account);
        }
        return account;
    }

    @Override
    public Set<Account> findByUserId(long id) {
        var accounts = ram.get(id);
        if (accounts.isEmpty()) {
            accounts = new HashSet<>(accountDao.findAllByUserId(id));
        }
        return accounts;
    }
}
