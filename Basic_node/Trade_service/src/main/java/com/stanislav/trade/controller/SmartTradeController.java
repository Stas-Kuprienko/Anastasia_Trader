package com.stanislav.trade.controller;

import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.TimeFrame;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.service.AccountService;
import com.stanislav.trade.controller.form.SmartSubscriptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/smart")
public class SmartTradeController {

    private final AccountService accountService;
    private final SmartAutoTradeService smartAutoTradeService;

    @Autowired
    public SmartTradeController(AccountService accountService, SmartAutoTradeService smartAutoTradeService) {
        this.accountService = accountService;
        this.smartAutoTradeService = smartAutoTradeService;
    }


    @GetMapping("/strategies")
    public Set<String> selectSmart() {
        return smartAutoTradeService.getStrategies();
    }

    @PostMapping("/{userId}/accounts/{account}/subscribe/{strategy}")
    public ResponseEntity<SmartSubscriptionResponse> subscribe(@PathVariable("userId") Long userId,
                                                               @PathVariable("account") String accountParam,
                                                               @PathVariable("strategy") String strategy,
                                                               @RequestParam("ticker") String ticker,
                                                               @RequestParam("board") String board,
                                                               @RequestParam(value = "time_frame", required = false) String timeFrame) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account data = " + accountParam);
        }
        //TODO TEMPORARY, JUST FOR SAMPLE
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());
        TimeFrame.Scope tf = TimeFrame.valueOf(timeFrame);
        //TODO
        smartAutoTradeService.subscribe(
                clientId,
                broker,
                ticker,
                Board.valueOf(board),
                strategy,
                tf,
                token);
        SmartSubscriptionResponse response = new SmartSubscriptionResponse();
        return ResponseEntity.ok(response);
    }

}
