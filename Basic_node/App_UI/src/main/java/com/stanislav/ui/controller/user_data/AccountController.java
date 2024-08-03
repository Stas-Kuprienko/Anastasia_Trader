package com.stanislav.ui.controller.user_data;

import com.stanislav.ui.configuration.WebApplicationUIConfig;
import com.stanislav.ui.configuration.auth.form.MyUserDetails;
import com.stanislav.ui.controller.service.MVC;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.model.user.Portfolio;
import com.stanislav.ui.service.AccountService;
import com.stanislav.ui.service.TradingService;
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

    private final AccountService accountService;
    private final TradingService tradingService;

    @Autowired
    public AccountController(AccountService accountService, TradingService tradingService) {
        this.accountService = accountService;
        this.tradingService = tradingService;
    }


    @GetMapping("/account/{clientId}")
    public String getAccount(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("account") String accountParams, Model model) {

        String[] accountData = accountParams.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParams);
        }
        String broker = accountData[0];
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();

        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        Portfolio portfolio = tradingService.getPortfolio(userId, account.getBroker(), account.getClientId(), true);

        model.addAttribute("account", account);
        model.addAttribute("balance", portfolio.balance());
        model.addAttribute("positions", portfolio.positions());
        //TODO smart trade subscribes
        return "account";
    }

    @GetMapping("/new-account")
    public String newAccountPage(Model model) {
        model.addAttribute("brokers", List.of(Broker.values()));
        return "new_account";
    }

    @PostMapping("/account")
    public String newAccountHandle(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("clientId") String clientId,
                                   @RequestParam("token") String token,
                                   @RequestParam("broker") String broker,
                                   Model model) {
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.createAccount(userId, clientId, broker, token);
        var accounts = accountService.findByUserId(userId);
        accounts.add(account);
        model.addAttribute("accounts", accounts);
        return MVC.REDIRECT + WebApplicationUIConfig.resource +
                "user/account/" + clientId + "?broker=" + broker;
    }


    @GetMapping("/accounts")
    public String getAccounts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        long userId = ((MyUserDetails) userDetails).getId();
        List<Account> accounts = accountService.findByUserId(userId);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @DeleteMapping("/account/{account}")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable("account") String accountParams) {
        String[] accountData = accountParams.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParams);
        }
        String broker = accountData[0];
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();

        accountService.deleteAccount(userId, clientId, Broker.valueOf(broker));
        return MVC.REDIRECT + WebApplicationUIConfig.resource + "user/accounts";
    }
}
