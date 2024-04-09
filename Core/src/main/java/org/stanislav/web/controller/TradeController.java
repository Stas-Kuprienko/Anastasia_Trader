package org.stanislav.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.stanislav.database.DatabaseRepository;
import org.stanislav.entities.orders.Direction;
import org.stanislav.entities.orders.Order;
import org.stanislav.entities.user.Account;
import org.stanislav.entities.user.User;
import org.stanislav.domain.trading.TradingService;

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
    @Qualifier("hibernateRepository")
    private DatabaseRepository databaseRepository;

    @GetMapping(value = "/orders")
    public List<Order> getOrders(@RequestParam boolean includeMatched, @RequestParam boolean includeCanceled, @RequestParam boolean includeActive) {
        Account account = databaseRepository.accountRepository().getById(id); //temporary. just for testing

        return tradingService.getOrders(account, includeMatched, includeCanceled, includeActive);
    }

    @PostMapping("/order")
    public Order newOrder(@RequestParam String login,
                          @RequestParam String ticker,
                          @RequestParam double price,
                          @RequestParam int quantity,
                          @RequestParam String direction) {
        //TODO
        User user = databaseRepository.userRepository().getById(login);
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
