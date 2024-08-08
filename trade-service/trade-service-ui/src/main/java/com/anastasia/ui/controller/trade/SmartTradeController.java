package com.anastasia.ui.controller.trade;

import com.anastasia.ui.configuration.auth.form.MyUserDetails;
import com.anastasia.ui.model.Board;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.TimeFrame;
import com.anastasia.ui.model.user.Account;
import com.anastasia.ui.service.AccountService;
import com.anastasia.ui.service.SmartAutoTradeService;
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
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/smart")
public class SmartTradeController {

    private final AccountService accountService;
    private final SmartAutoTradeService smartAutoTradeService;

    @Autowired
    public SmartTradeController(AccountService accountService, SmartAutoTradeService smartAutoTradeService) {
        this.accountService = accountService;
        this.smartAutoTradeService = smartAutoTradeService;
    }


    @GetMapping("/select")
    public String selectSmart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Account> accounts = accountService
                .findByUserId(((MyUserDetails) userDetails).getId());

        Set<String> strategies = smartAutoTradeService.getStrategies();
        model.addAttribute("accounts", accounts);
        model.addAttribute("strategies", strategies);
        return "select_smart";
    }

    @GetMapping("/subscribe")
    public String subscribePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        long userId = ((MyUserDetails) userDetails).getId();
        List<Account> accounts = accountService.findByUserId(userId);
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
        }
        //TODO TEMPORARY, JUST FOR SAMPLE
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        TimeFrame.Scope tf = TimeFrame.valueOf(timeFrame);
        smartAutoTradeService
                .subscribe(userId, account.getClientId(), account.getBroker(), "SBER", Board.TQBR, strategy, tf);
        return "ok";
    }

}
