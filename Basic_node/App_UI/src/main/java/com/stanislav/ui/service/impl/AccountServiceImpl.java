package com.stanislav.ui.service.impl;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.model.user.User;
import com.stanislav.ui.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Override
    public Account createAccount(User user, String clientId, String token, String broker) {

        return null;
    }

    @Override
    public Account findByClientIdAndBroker(long userId, String clientId, Broker broker) {
        return null;
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Account> findByLogin(String userLogin) {
        return null;
    }

    @Override
    public void deleteAccount(String userLogin, Long accountId) {

    }
}
