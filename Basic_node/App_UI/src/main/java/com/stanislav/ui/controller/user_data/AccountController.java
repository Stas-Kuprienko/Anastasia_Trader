package com.stanislav.ui.controller.user_data;

import com.stanislav.ui.configuration.WebApplicationUIConfig;
import com.stanislav.ui.configuration.auth.form.MyUserDetails;
import com.stanislav.ui.controller.service.ErrorCase;
import com.stanislav.ui.controller.service.ErrorController;
import com.stanislav.ui.controller.service.MVC;
import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.user.User;
import com.stanislav.ui.service.AccountService;
import com.stanislav.ui.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/new-account")
    public String newAccount(Model model) {
        model.addAttribute("brokers", List.of(Broker.values()));
        return "new_account";
    }

    @GetMapping("/account/{clientId}")
    public String getAccount(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("clientId") String clientId,
                             @RequestParam("broker") String broker, Model model) {

        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        String token = accountService.decodeToken(account.getToken());
//        Portfolio portfolio = tradingService.getPortfolio(account.getClientId(), token, true);
        model.addAttribute("account", account);
//        model.addAttribute("balance", portfolio.getBalance());
//        model.addAttribute("positions", portfolio.getPositions());
        //TODO smart trade subscribes
        return "account";
    }

    @PostMapping("/account")
    public String newAccountHandle(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("clientId") String clientId,
                                   @RequestParam("token") String token,
                                   @RequestParam("broker") String broker,
                                   Model model) {
        User user = userService.findByLogin(userDetails.getUsername());
        try {
            Account account = accountService.createAccount(user, clientId, token, broker);
            var accounts = accountService.findByLogin(userDetails.getUsername());
            accounts.add(account);
            model.addAttribute("accounts", accounts);
            return MVC.REDIRECT + WebApplicationUIConfig.resource +
                    "user/account/" + clientId + "?broker=" + broker;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ErrorController.REDIRECT_ERROR + ErrorCase.BAD_REQUEST;
        }
    }


    @GetMapping("/accounts")
    public String getAccounts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            List<Account> accounts = accountService.findByLogin(userDetails.getUsername());
            model.addAttribute("accounts", accounts);
            return "accounts";
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return MVC.FORWARD + MVC.LOGIN_PAGE;
        }
    }

    @DeleteMapping("/account/{account}")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable("account") Long accountId) {
        accountService.deleteAccount(userDetails.getUsername(), accountId);
        return MVC.REDIRECT + WebApplicationUIConfig.resource + "user/accounts";
    }
}
