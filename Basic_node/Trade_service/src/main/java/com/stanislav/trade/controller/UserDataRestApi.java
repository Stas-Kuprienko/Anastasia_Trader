package com.stanislav.trade.controller;

import com.stanislav.trade.controller.form.LogInUserForm;
import com.stanislav.trade.controller.form.SignUpUserForm;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.service.AccountService;
import com.stanislav.trade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserDataRestApi {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserDataRestApi(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @PostMapping("/user")
    public ResponseEntity<User> signUp(SignUpUserForm form) {
        User user = userService.createUser(
                form.login(),
                form.password(),
                form.name());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    public ResponseEntity<User> logIn(LogInUserForm form) {
        User user = userService.findUserByLoginAndPassword(form.login(), form.password());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<Account>> getAccounts(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(accountService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/accounts/account/{account}")
    public ResponseEntity<Account> getAccount(@PathVariable("userId") Long userId,
                                              @PathVariable("account") String accountParams) {
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