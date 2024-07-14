package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.TimeFrame;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/smart")
public class SmartTradeController {

    private final UserDataService userDataService;
    private final SmartAutoTradeService smartAutoTradeService;

    @Autowired
    public SmartTradeController(UserDataService userDataService, SmartAutoTradeService smartAutoTradeService) {
        this.userDataService = userDataService;
        this.smartAutoTradeService = smartAutoTradeService;
    }

    @GetMapping("/select")
    public String selectSmart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<User> user = userDataService.findUserByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error("user=" + userDetails.getUsername() + " is lost");
            return "login";
        }
        Set<String> strategies = smartAutoTradeService.getStrategies();
        model.addAttribute("accounts", user.get().getAccounts());
        model.addAttribute("strategies", strategies);
        return "select_smart";
    }

    @GetMapping("/subscribe")
    public String subscribePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Account> accounts = userDataService.getAccountsByLogin(userDetails.getUsername());
        model.addAttribute("accounts", accounts);
        return "smart";
    }

    @PostMapping("/subscribe")
    public String subscribeToStrategy(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam("account") String accountParam,
                                      @RequestParam("time_frame") String timeFrame,
                                      @RequestParam("strategy") String strategy) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account data = " + accountParam);
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
        //TODO TEMPORARY, JUST FOR SAMPLE
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Optional<Account> optionalAccount = userDataService.findAccountByLoginClientBroker(userDetails.getUsername(), clientId, broker);
        if (optionalAccount.isEmpty()) {
            return ErrorController.URL_FORWARD + ErrorCase.NOT_FOUND;
        }
        Account account = optionalAccount.get();
        String token = userDataService.decodeToken(account.getToken());
        TimeFrame.Scope tf = TimeFrame.valueOf(timeFrame);
        smartAutoTradeService.subscribe(account.getClientId(), account.getBroker(), "SBER", Board.TQBR, strategy, tf, token);
        return "ok";
    }

}
