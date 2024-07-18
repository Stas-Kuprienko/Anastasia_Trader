package com.stanislav.trade.web.rest;

import com.stanislav.trade.entities.user.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class AccountsRestApi {


    @GetMapping("/accounts")
    public Account[] getAccounts(@RequestParam Long id) {
        return null;
    }
}