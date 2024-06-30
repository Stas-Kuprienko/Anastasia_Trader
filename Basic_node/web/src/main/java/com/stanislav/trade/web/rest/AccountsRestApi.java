package com.stanislav.trade.web.rest;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AccountsRestApi {

    private final UserDataService userDataService;
    private final AccountService accountService;

    @Autowired
    public AccountsRestApi(UserDataService userDataService, AccountService accountService) {
        this.userDataService = userDataService;
        this.accountService = accountService;
    }


    @GetMapping("/accounts")
    public Account[] getAccounts(@RequestParam Long id) {
        User user = userDataService.findById(id).orElseThrow();
        return accountService.findByUser(user.getId()).toArray(new Account[]{});
    }
}