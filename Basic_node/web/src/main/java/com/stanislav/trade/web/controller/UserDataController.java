package com.stanislav.trade.web.controller;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserDataController {

    private final ErrorDispatcher errorDispatcher;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public UserDataController(ErrorDispatcher errorDispatcher, UserService userService, AccountService accountService) {
        this.errorDispatcher = errorDispatcher;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/new-account")
    public String newAccount() {
        return "new_account";
    }

    @GetMapping("/account/{account-id}")
    public String getAccount(@PathVariable("account-id") Long accountId) {
        return "account";
    }

    @PostMapping("/account")
    public String newAccountHandle(HttpSession session,
                             @RequestParam("clientId") String clientId,
                             @RequestParam("token") String token,
                             @RequestParam("broker") String broker,
                             Model model) {

        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            //TODO error
            return errorDispatcher.apply(0);
        }
        User user = userService.findById(id).orElseThrow();
        accountService.create(user, clientId, token, broker);
        Set<Account> accounts = accountService.findByUser(user);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @GetMapping("/accounts")
    public String getAccounts(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            //TODO error
            return errorDispatcher.apply(0);
        }
        User user = userService.findById(id).orElseThrow();
        Set<Account> accounts = accountService.findByUser(user);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}
