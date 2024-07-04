/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.trading.OrderCriteria;
import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.FinamOrderTradeCriteria;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.service.ErrorCase;
import com.stanislav.trade.web.service.OrderService;
import com.stanislav.trade.web.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final OrderService orderService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;


    @Autowired
    public TradeController(List<TradingService> tradingServices, UserDataService userDataService,
                           AccountService accountService, OrderService orderService) {
        this.tradingServiceMap = initTradingServiceMap(tradingServices);
        this.userDataService = userDataService;
        this.accountService = accountService;
        this.orderService = orderService;
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
                    accountService.decodeToken(account.getToken()),
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

    @PostMapping("/order/{accountId}")
    public String newOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("ticker") String ticker,
                           @RequestParam String accountId,
                           @RequestParam String board,
                           @RequestParam String market,
                           @RequestParam double price,
                           @RequestParam int quantity,
                           @RequestParam String direction, Model model) {
        long id;
        Account account;
        try {
            id = Long.parseLong(accountId);
            account = accountService.findById(id, userDetails.getUsername()).orElseThrow();
        } catch (NumberFormatException | NullPointerException | NoSuchElementException e) {
            log.info(e.getMessage());
            return ErrorController.URL + ErrorCase.BAD_REQUEST;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL + ErrorCase.ACCESS_DENIED;
        }
        OrderCriteria criteria = OrderCriteria.builder()
                .broker(account.getBroker())
                .clientId(account.getClientId())
                .ticker(ticker)
                .board(Board.valueOf(board))
                .quantity(quantity)
                .price(price)
                .direction(Direction.valueOf(direction))
                .isMargin(false)
                .build();
        String token = accountService.decodeToken(account.getToken());
        TradingService tradingService = tradingServiceMap.get(account.getBroker());
        Order order = tradingService.makeOrder(criteria, token);
        orderService.save(order);
        model.addAttribute("order", order);
        //TODO message
        return "order";
    }


    private ConcurrentHashMap<Broker, TradingService> initTradingServiceMap(List<TradingService> tradingServices) {
        ConcurrentHashMap<Broker, TradingService> map = new ConcurrentHashMap<>();
        for (TradingService t : tradingServices) {
            map.put(t.getBroker(), t);
        }
        return map;
    }
}
