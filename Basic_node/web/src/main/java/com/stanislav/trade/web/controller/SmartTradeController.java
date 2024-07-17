package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.TimeFrame;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.web.authentication.form.MyUserDetails;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.UserService;
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

    private final UserService userDataService;
    private final AccountService accountService;
    private final SmartAutoTradeService smartAutoTradeService;

    @Autowired
    public SmartTradeController(UserService userDataService,
                                AccountService accountService, SmartAutoTradeService smartAutoTradeService) {
        this.userDataService = userDataService;
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
        List<Account> accounts = accountService.findByLogin(userDetails.getUsername());
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
            return ErrorController.FORWARD_ERROR + ErrorCase.BAD_REQUEST;
        }
        //TODO TEMPORARY, JUST FOR SAMPLE
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());
        TimeFrame.Scope tf = TimeFrame.valueOf(timeFrame);
        smartAutoTradeService.subscribe(account.getClientId(), account.getBroker(), "SBER", Board.TQBR, strategy, tf, token);
        return "ok";
    }

}
