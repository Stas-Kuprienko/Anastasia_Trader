package com.stanislav.trade.web.rest;

import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserDataRestApi {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserDataRestApi(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts(@RequestParam Long userId) {
        return accountService.findByUserId(userId);
    }

    @GetMapping("/account/{account}")
    public ResponseEntity<Account> getAccount(@PathVariable("account") String accountParams, @RequestParam("userId") Long userId) {
        String[] accountData = accountParams.split(":");
        if (accountData.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        return ResponseEntity.ok(account);
    }
}