package com.stanislav.trade.web.controller;

import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
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

    @GetMapping("/account/{account}")
    public String getAccount(@PathVariable("account") String accountId,
                             HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.error("User ID is lost");
            return "redirect:/login";
        }
        long accId;
        try {
            accId = Long.parseLong(accountId);
        } catch (NumberFormatException | NullPointerException e) {
            log.info(e.getMessage());
            return "forward:/error/" + ErrorCase.BAD_REQUEST;
        }
        var optionalUser = userDataService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Account> account = accountService.findById(user, accId);
            if (account.isPresent()) {
                model.addAttribute("account", account.get());
                return "account";
            } else {
                return "forward:/error/" + ErrorCase.NOT_FOUND;
            }
        }
        log.error("User not found: " + id);
        return "redirect:/login";
    }

    @PostMapping("/account")
    public String newAccountHandle(HttpSession session,
                                   @RequestParam("clientId") String clientId,
                                   @RequestParam("token") String token,
                                   @RequestParam("broker") String broker,
                                   Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.error("User ID is lost");
            return "redirect:/login";
        }
        Optional<User> user = userDataService.findById(id);
        if (user.isPresent()) {
            accountService.create(user.get(), clientId, token, broker);
            Set<Account> accounts = accountService.findByUser(user.get());
            model.addAttribute("accounts", accounts);
            return "accounts";
        } else {
            log.error("User not found: " + id);
            return "redirect:/login";
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(HttpSession session, Model model) {
        Long id = (Long) session.getAttribute("id");
        if (id == null) {
            log.error("User ID is lost");
            return "redirect:/login";
        }
        var optionalUser = userDataService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Account> accounts;
            if (user.getAccounts() != null) {
                accounts = accountService.findByUser(user);
            } else {
                accounts = new HashSet<>();
            }
            model.addAttribute("accounts", accounts);
            return "accounts";
        } else {
            log.error("User not found: " + id);
            return "redirect:/login";
        }
    }
}
