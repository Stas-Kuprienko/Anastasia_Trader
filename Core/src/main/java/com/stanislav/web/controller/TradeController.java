package com.stanislav.web.controller;

import com.stanislav.database.AccountPersistence;
import com.stanislav.database.UserPersistence;
import com.stanislav.domain.trading.TradingService;
import com.stanislav.entities.orders.Direction;
import com.stanislav.entities.orders.Order;
import com.stanislav.entities.user.Account;
import com.stanislav.entities.user.User;
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
    private AccountPersistence accountPersistence;

    @Autowired
    private UserPersistence userPersistence;

    @GetMapping(value = "/orders")
    public List<Order> getOrders(@RequestParam boolean includeMatched, @RequestParam boolean includeCanceled, @RequestParam boolean includeActive) {
        Account account = accountPersistence.getById(id); //temporary. just for testing

        return tradingService.getOrders(account, includeMatched, includeCanceled, includeActive);
    }

    @PostMapping("/order")
    public Order newOrder(@RequestParam String login,
                          @RequestParam String ticker,
                          @RequestParam double price,
                          @RequestParam int quantity,
                          @RequestParam String direction) {
        //TODO
        User user = userPersistence.getById(login);
        Account account = user.getAccounts().get(0);
        Order order = Order.builder()
                .account(account)
                .ticker(ticker)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .direction(Direction.parse(direction))
                .build();

        tradingService.makeOrder(account, order);
        return order;
    }

    @GetMapping("/order")
    public Order testOrder() {
        return Order.builder().build();
    }
}
