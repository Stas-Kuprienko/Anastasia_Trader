package com.stanislav.trade.web.controller.user_data;

import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.configuration.WebApplicationConfig;
import com.stanislav.trade.web.controller.TradeController;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.MVC;
import com.stanislav.trade.web.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/user")
public class AccountController {

    private final UserDataService userDataService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;

    @Autowired
    public AccountController(List<TradingService> tradingServices, UserDataService userDataService) {
        this.userDataService = userDataService;
        tradingServiceMap = TradeController.initTradingServiceMap(tradingServices);
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
        Optional<Account> optionalAccount = userDataService
                .findAccountByLoginClientBroker(userDetails.getUsername(), clientId, Broker.valueOf(broker));
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String token = userDataService.decodeToken(account.getToken());
            TradingService tradingService = tradingServiceMap.get(account.getBroker());
            Portfolio portfolio = tradingService.getPortfolio(account.getClientId(), token, true);
            model.addAttribute("account", account);
            model.addAttribute("balance", portfolio.getBalance());
            model.addAttribute("positions", portfolio.getPositions());
            //TODO smart trade subscribes
            return "account";
        } else {
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
    }

    @PostMapping("/account")
    public String newAccountHandle(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam("clientId") String clientId,
                                   @RequestParam("token") String token,
                                   @RequestParam("broker") String broker,
                                   Model model) {
        Optional<User> user = userDataService.findUserByLogin(userDetails.getUsername());
        if (user.isPresent()) {
            try {
                Account account = userDataService.createAccount(user.get(), clientId, token, broker);
                var accounts = userDataService.getAccountsByLogin(userDetails.getUsername());
                accounts.add(account);
                model.addAttribute("accounts", accounts);
                return MVC.REDIRECT + WebApplicationConfig.resource +
                        "user/account/" + clientId + "?broker=" + broker;
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                return ErrorController.URL_REDIRECT + ErrorCase.BAD_REQUEST;
            }
        } else {
            log.error("user=" + userDetails.getUsername() + " is lost");
            return MVC.REDIRECT + MVC.LOGIN_PAGE;
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            List<Account> accounts = userDataService.getAccountsByLogin(userDetails.getUsername());
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
        userDataService.deleteAccount(userDetails.getUsername(), accountId);
        return MVC.REDIRECT + WebApplicationConfig.resource + "user/accounts";
    }
}
