package com.stanislav.trade.controller;

import com.stanislav.trade.controller.form.LogInUserForm;
import com.stanislav.trade.controller.form.SignUpUserForm;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.model.AccountDto;
import com.stanislav.trade.model.UserDto;
import com.stanislav.trade.model.convertors.AccountDtoConverter;
import com.stanislav.trade.model.convertors.UserDtoConvertor;
import com.stanislav.trade.service.AccountService;
import com.stanislav.trade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserDataController {

    private final UserService userService;
    private final AccountService accountService;
    private final UserDtoConvertor userDtoConvertor;
    private final AccountDtoConverter accountDtoConverter;

    @Autowired
    public UserDataController(UserService userService,
                              AccountService accountService,
                              UserDtoConvertor userDtoConvertor,
                              AccountDtoConverter accountDtoConverter) {
        this.userService = userService;
        this.userDtoConvertor = userDtoConvertor;
        this.accountService = accountService;
        this.accountDtoConverter = accountDtoConverter;
    }


    @PostMapping("/user")
    public ResponseEntity<User> signUp(SignUpUserForm form) {
        User user = userService.createUser(
                form.login(),
                form.password(),
                form.name());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/login")
    public ResponseEntity<UserDto> logIn(LogInUserForm form) {
        User user = userService.findUserByLoginAndPassword(form.login(), form.password());
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByLogin(@RequestParam("login") String login) {
        User user = userService.findUserByLogin(login);
        return ResponseEntity.ok(userDtoConvertor.convert(user));
    }

    @GetMapping("/user/{userId}/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(@PathVariable("userId") Long userId) {
        List<Account> accounts = accountService.findByUserId(userId);
        List<AccountDto> dtoList = new ArrayList<>();
        for (Account a : accounts) {
            dtoList.add(accountDtoConverter.convert(a));
        }
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/user/{userId}/accounts/account/{account}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("userId") Long userId,
                                              @PathVariable("account") String accountParams) {
        String[] accountData = accountParams.split(":");
        if (accountData.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        return ResponseEntity.ok(accountDtoConverter.convert(account));
    }
}