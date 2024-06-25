package com.stanislav.trade.web.rest;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AccountsRestApi {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountsRestApi(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @GetMapping("/accounts")
    public Account[] getAccounts(@RequestParam Long id) {
        User user = userService.findById(id).orElseThrow();
        return accountService.findByUser(user).toArray(new Account[]{});
    }
}