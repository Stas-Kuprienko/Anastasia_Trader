/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.FinamOrderTradeCriteria;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.ErrorCase;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/trade")
public final class TradeController {

    private final UserDataService userDataService;
    private final AccountService accountService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;


    @Autowired
    public TradeController(List<TradingService> tradingServices, UserDataService userDataService, AccountService accountService) {
        this.userDataService = userDataService;
        this.accountService = accountService;
        this.tradingServiceMap = initTradingServiceMap(tradingServices);
    }


    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam String clientId,
                            @RequestParam boolean includeMatched,
                            @RequestParam boolean includeCanceled,
                            @RequestParam boolean includeActive,
                            Model model) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error(userDetails.getUsername() + " is not found");
            return "login";
        }
        try {
            Account account = user.get().getAccounts()
                    .stream()
                    .filter(e -> e.getClientId().equals(clientId))
                    .findFirst()
                    .orElseThrow();
            TradingService tradingService;
            tradingService = tradingServiceMap.get(account.getBroker());
            var orders = tradingService.getOrders(
                    account.getClientId(),
                    accountService.decodeToken(account),
                    includeMatched,
                    includeCanceled,
                    includeActive);
            model.addAttribute("orders", orders);
            return "orders";
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
            log.error(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        }
    }

    @GetMapping("/order")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam String clientId,
                           @RequestParam int orderId) {

        return "order";
    }

    @PostMapping("/order")
    public String newOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam String clientId,
                           @RequestParam String ticker,
                           @RequestParam double price,
                           @RequestParam int quantity,
                           @RequestParam String direction,
                           Model model) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error(userDetails.getUsername() + " is not found");
            return "login";
        }
        try {
            Direction dir = Direction.valueOf(direction);
            Account account = user.get().getAccounts()
                    .stream()
                    .filter(e -> e.getClientId().equals(clientId))
                    .findFirst()
                    .orElseThrow();
            Order order = Order.builder()
                    .account(account)
                    .ticker(ticker)
                    .price(BigDecimal.valueOf(price))
                    .quantity(quantity)
                    .direction(dir)
                    .build();
            TradingService tradingService = tradingServiceMap.get(account.getBroker());
            TradeCriteria tradeCriteria = FinamOrderTradeCriteria.simpleOrderAtMarketPrice(dir);
            tradingService.makeOrder(order, tradeCriteria);
            model.addAttribute("order", order);
            //TODO message
            return "order";
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        }
    }


    private ConcurrentHashMap<Broker, TradingService> initTradingServiceMap(List<TradingService> tradingServices) {
        ConcurrentHashMap<Broker, TradingService> map = new ConcurrentHashMap<>();
        for (TradingService t : tradingServices) {
            map.put(t.getBroker(), t);
        }
        return map;
    }
}
