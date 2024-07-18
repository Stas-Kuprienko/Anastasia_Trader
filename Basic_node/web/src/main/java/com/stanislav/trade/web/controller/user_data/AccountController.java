package com.stanislav.trade.web.controller.user_data;

import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.authentication.form.MyUserDetails;
import com.stanislav.trade.web.configuration.WebApplicationConfig;
import com.stanislav.trade.web.controller.TradeController;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.MVC;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/user")
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;

    @Autowired
    public AccountController(List<TradingService> tradingServices,
                             UserService userService, AccountService accountService) {
        this.userService = userService;
        tradingServiceMap = TradeController.initTradingServiceMap(tradingServices);
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
        TradingService tradingService = tradingServiceMap.get(account.getBroker());
        Portfolio portfolio = tradingService.getPortfolio(account.getClientId(), token, true);
        model.addAttribute("account", account);
        model.addAttribute("balance", portfolio.getBalance());
        model.addAttribute("positions", portfolio.getPositions());
        //TODO smart trade subscribes
        return "account";
    }

    @PostMapping("/account")
    public String newAccountHandle(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("clientId") String clientId,
                                   @RequestParam("token") String token,
                                   @RequestParam("broker") String broker,
                                   Model model) {
        User user = userService.findUserByLogin(userDetails.getUsername());
        try {
            Account account = accountService.createAccount(user, clientId, token, broker);
            var accounts = accountService.findByLogin(userDetails.getUsername());
            accounts.add(account);
            model.addAttribute("accounts", accounts);
            return MVC.REDIRECT + WebApplicationConfig.resource +
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
        return MVC.REDIRECT + WebApplicationConfig.resource + "user/accounts";
    }
}
