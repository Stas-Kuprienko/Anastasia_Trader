package com.stanislav.trade.web.controller;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.ErrorDispatcher;
import com.stanislav.trade.web.service.UserService;
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
            log.warn("Session scope attribute user ID is null");
            return "login";
        }
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            accountService.create(user.get(), clientId, token, broker);
            Set<Account> accounts = accountService.findByUser(user.get());
            model.addAttribute("accounts", accounts);
            return "accounts";
        } else {
            String message = errorDispatcher.apply(ErrorCase.NO_SUCH_USER);
            model.addAttribute("message", message);
            return ErrorDispatcher.ERROR_PAGE;
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.warn("Session scope attribute user ID is null");
            return "login";
        }
        User user = userService.findById(id).orElseThrow();
        Set<Account> accounts = accountService.findByUser(user);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}
