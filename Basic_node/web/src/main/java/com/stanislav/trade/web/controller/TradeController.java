/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.datasource.AccountDao;
import com.stanislav.trade.datasource.UserDao;
import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.FinamOrderTradeCriteria;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/trade")
public final class TradeController {

    @Value("${id.finam}") String id; //temporary. just for testing

    @Autowired
    @Qualifier("finamTradingService")
    private TradingService tradingService;

    @Autowired
    private AccountDao accountPersistence;

    @Autowired
    private UserDao userPersistence;

    @GetMapping(value = "/orders")
    public List<Order> getOrders(@RequestParam boolean includeMatched, @RequestParam boolean includeCanceled, @RequestParam boolean includeActive) {
        Account account = accountPersistence.findById(0L).get(); //temporary. just for testing

        return tradingService.getOrders(account, includeMatched, includeCanceled, includeActive);
    }

    @PostMapping("/order")
    public Order newOrder(@RequestParam String login,
                          @RequestParam String ticker,
                          @RequestParam double price,
                          @RequestParam int quantity,
                          @RequestParam String direction) {
        //TODO
        User user = userPersistence.findById(0L).get();
        Account account = user.getAccounts().get(0);
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
