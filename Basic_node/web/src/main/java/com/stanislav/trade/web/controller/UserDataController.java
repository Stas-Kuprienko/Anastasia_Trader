package com.stanislav.trade.web.controller;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserDataController {

    private final UserDataService userDataService;
    private final AccountService accountService;

    @Autowired
    public UserDataController(UserDataService userDataService, AccountService accountService) {
        this.userDataService = userDataService;
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
            log.warn("Session scope attribute user ID is null");
            return "login";
        }
        Optional<User> user = userDataService.findById(id);
        if (user.isPresent()) {
            accountService.create(user.get(), clientId, token, broker);
            Set<Account> accounts = accountService.findByUser(user.get());
            model.addAttribute("accounts", accounts);
            return "accounts";
        } else {
            log.error("User not found: " + user);
            return "redirect:/login";
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.warn("Session scope attribute user ID is null");
            return "login";
        }
        User user = userDataService.findById(id).orElseThrow();
        Set<Account> accounts = accountService.findByUser(user);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}
