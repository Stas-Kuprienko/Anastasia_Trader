package com.stanislav.trade.web.controller.user_data;

import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/user")
public class AccountController {

    private final UserDataService userDataService;
    private final AccountService accountService;

    private final TradingService tradingService;

    @Autowired
    public AccountController(UserDataService userDataService, AccountService accountService,
                             @Qualifier("finamTradingService") TradingService tradingService) {
        this.userDataService = userDataService;
        this.accountService = accountService;
        this.tradingService = tradingService;
    }

    @GetMapping("/new-account")
    public String newAccount(Model model) {
        model.addAttribute("brokers", List.of(Broker.values()));
        return "new_account";
    }

    @GetMapping("/account/{account}")
    public String getAccount(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("account") String accountId, Model model) {
        long id;
        Optional<Account> account;
        try {
            id = Long.parseLong(accountId);
            account = accountService.findById(id, userDetails.getUsername());
        } catch (NumberFormatException | NullPointerException e) {
            log.info(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL + ErrorCase.ACCESS_DENIED;
        }
        if (account.isPresent()) {
            model.addAttribute("account", account.get());
            return "account";
        } else {
            log.info("account is not found, id=" + id);
            return ErrorController.URL + ErrorCase.NOT_FOUND;
        }
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
            try {
                Account account = accountService.create(user.get(), clientId, token, broker);
                var accounts = user.get().getAccounts();
                accounts.add(account);
                model.addAttribute("accounts", accounts);
                return "accounts";
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                return ErrorController.URL + ErrorCase.BAD_REQUEST;
            }
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
            if (user.getAccounts() == null) {
                accounts = accountService.findByUser(user.getId());
            } else {
                accounts = new HashSet<>(user.getAccounts());
            }
            model.addAttribute("accounts", accounts);
            return "accounts";
        } else {
            log.error("User not found: " + id);
            return "redirect:/login";
        }
    }

    @DeleteMapping("/account/{account}")
    public String deleteAccount(@PathVariable("account") String accountId, Model model) {
        long id;
        try {
            id = Long.parseLong(accountId);
        } catch (NumberFormatException | NullPointerException e) {
            log.info(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        }
        accountService.delete(id);
        var accounts = accountService.findByUser(id);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @GetMapping("/account/{accountId}/portfolio")
    public String portfolio(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("accountId") String accountId, Model model) {
        long id;
        Optional<Account> account;
        try {
            id = Long.parseLong(accountId);
            account = accountService.findById(id, userDetails.getUsername());
        } catch (NumberFormatException | NullPointerException e) {
            log.info(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL + ErrorCase.ACCESS_DENIED;
        }
        if (account.isPresent()) {
            Account a = account.get();
            String token = accountService.decodeToken(a.getToken());
            Portfolio portfolio = tradingService.getPortfolio(a.getClientId(), token, true);
            model.addAttribute("portfolio", portfolio);
            //TODO to create page
            return "portfolio";
        } else {
            log.info("account is not found, id=" + id);
            return ErrorController.URL + ErrorCase.NOT_FOUND;
        }
    }
}
