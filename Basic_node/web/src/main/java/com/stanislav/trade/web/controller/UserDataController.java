package com.stanislav.trade.web.controller;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/account")
    public String newAccount() {
        return "new_account";
    }

    @PostMapping("/account")
    public String newAccountHandle(HttpSession session,
                             @RequestParam String clientId,
                             @RequestParam String token,
                             @RequestParam String broker,
                             Model model) {

        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            //TODO error
            return errorDispatcher.apply(0);
        }
        User user = userService.findById(id).orElseThrow();
        accountService.create(user, clientId, token, broker);
        Set<Account> accounts = accountService.findByUserId(id);
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
        Set<Account> accounts = accountService.findByUserId(id);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}
