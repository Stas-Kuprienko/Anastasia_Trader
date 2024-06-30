/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.FinamOrderTradeCriteria;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/trade")
public final class TradeController {

    @Autowired
    @Qualifier("finamTradingService")
    private TradingService tradingService;

    @Autowired
    private UserDataService userDataService;


    @GetMapping(value = "/orders")
    public List<Order> getOrders(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String clientId,
                                 @RequestParam boolean includeMatched,
                                 @RequestParam boolean includeCanceled,
                                 @RequestParam boolean includeActive) {
        //TODO
        User user = userDataService.findByLogin(userDetails.getUsername()).orElseThrow();
        Account account = user.getAccounts()
                .stream()
                .filter(e -> e.getClientId().equals(clientId))
                .findFirst()
                .orElseThrow();

        return tradingService.getOrders(account, includeMatched, includeCanceled, includeActive);
    }

    @PostMapping("/order")
    public Order newOrder(@RequestParam String login,
                          @RequestParam String ticker,
                          @RequestParam double price,
                          @RequestParam int quantity,
                          @RequestParam String direction) {
        //TODO
        Account account = null;
        Order order = Order.builder()
                .account(account)
                .ticker(ticker)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .direction(Direction.parse(direction))
                .build();

        TradeCriteria tradeCriteria = FinamOrderTradeCriteria.simpleOrderAtMarketPrice(Direction.Buy);
        tradingService.makeOrder(order, tradeCriteria);
        return order;
    }

    @GetMapping("/order")
    public Order testOrder() {
        return Order.builder().build();
    }
}
