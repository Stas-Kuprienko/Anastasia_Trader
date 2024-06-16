package com.stanislav.trade.web.rest;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.web.model.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AccountsRestApi {

    private final AccountDao accountDao;

    @Autowired
    public AccountsRestApi(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @GetMapping("/accounts")
    public AccountDto[] getAccounts(@RequestParam String login) {
        return null;
    }
}
