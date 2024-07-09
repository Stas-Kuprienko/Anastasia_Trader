package com.stanislav.trade.web.controller.user_data;

import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.TradeController;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.MVC;
import com.stanislav.trade.web.service.UserDataService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/account")
    public String getAccount(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("clientId") String clientId,
                             @RequestParam("broker") String broker, Model model) {
        Optional<Account> optionalAccount = userDataService
                .findAccountByLoginClientIdBroker(userDetails.getUsername(), clientId, Broker.valueOf(broker));
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String token = userDataService.decodeToken(account.getToken());
            TradingService tradingService = tradingServiceMap.get(account.getBroker());
            Portfolio portfolio = tradingService.getPortfolio(account.getClientId(), token, true);
            model.addAttribute("account", account);
            model.addAttribute("balance", portfolio.getBalance());
            model.addAttribute("positions", portfolio.getPositions());
            //TODO update page
            return "account";
        } else {
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
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
        Optional<User> user = userDataService.findUserById(id);
        if (user.isPresent()) {
            try {
                Account account = userDataService.createAccount(user.get(), clientId, token, broker);
                var accounts = user.get().getAccounts();
                accounts.add(account);
                model.addAttribute("accounts", accounts);
                return "accounts";
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage());
                return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
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
        var optionalUser = userDataService.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("accounts", user.getAccounts());
            return "accounts";
        } else {
            log.error("User not found: " + id);
            return "redirect:/login";
        }
    }

    @DeleteMapping("/account/{account}")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable("account") Long accountId, Model model) {
        userDataService.deleteAccount(userDetails.getUsername(), accountId);
        Optional<User> user = userDataService.findUserByLogin(userDetails.getUsername());
        if (user.isPresent()) {
            model.addAttribute("accounts", user.get().getAccounts());
            return MVC.REDIRECT + "accounts";
        } else {
            log.error("user=" + userDetails.getUsername() + " is lost");
            return MVC.REDIRECT + "login";
        }
    }
}
