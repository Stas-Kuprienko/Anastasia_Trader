package com.anastasia.trade.controller;

import com.anastasia.trade.controller.form.NewAccountForm;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.user.Account;
import com.anastasia.trade.model.AccountDto;
import com.anastasia.trade.model.convertors.AccountDtoConverter;
import com.anastasia.trade.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AccountDataController {

    private final AccountService accountService;
    private final AccountDtoConverter accountDtoConverter;

    @Autowired
    public AccountDataController(AccountService accountService, AccountDtoConverter accountDtoConverter) {
        this.accountService = accountService;
        this.accountDtoConverter = accountDtoConverter;
    }


    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(@PathVariable("userId") Long userId) {
        List<Account> accounts = accountService.findByUserId(userId);
        List<AccountDto> dtoList = new ArrayList<>();
        for (Account a : accounts) {
            dtoList.add(accountDtoConverter.convert(a));
        }
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{userId}/accounts/{account}")
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

    @PostMapping("/{userId}/accounts/account")
    public ResponseEntity<Account> newAccount(@PathVariable("userId") Long userId,
                                              @RequestBody NewAccountForm newAccountForm) {
        Broker broker = Broker.valueOf(newAccountForm.broker());
        Account account = accountService.createAccount(
                userId,
                newAccountForm.clientId(),
                newAccountForm.token(),
                broker.toString());
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{userId}/accounts/{account}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable("userId") Long userId,
                                                 @PathVariable("account") String accountParams) {
        String[] accountData = accountParams.split(":");
        if (accountData.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        accountService.deleteByClientIdAndBroker(userId, clientId, broker);
        return ResponseEntity.ok(true);
    }
}
