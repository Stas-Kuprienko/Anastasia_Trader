package com.anastasia.trade.controller;

import com.anastasia.trade.controller.form.NewOrderForm;
import com.anastasia.trade.domain.trading.OrderCriteria;
import com.anastasia.trade.domain.trading.TradingService;
import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Direction;
import com.anastasia.trade.entities.orders.Order;
import com.anastasia.trade.entities.user.Account;
import com.anastasia.trade.entities.user.Portfolio;
import com.anastasia.trade.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/trade")
public class TradingController {

    private final AccountService accountService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;

    @Autowired
    public TradingController(List<TradingService> tradingServices, AccountService accountService) {
        this.tradingServiceMap = new ConcurrentHashMap<>();
        for (var ts : tradingServices) {
            tradingServiceMap.put(ts.getBroker(), ts);
        }
        this.accountService = accountService;
    }


    @GetMapping("/{userId}/accounts/{account}/portfolio")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable("userId") Long userId,
                                                  @PathVariable("account") String accountParam,
                                                  @RequestParam(value = "withPositions", required = false, defaultValue = "false") boolean withPositions) {

        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException("Account parameters = '%s'".formatted(accountParam));
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());

        TradingService ts = tradingServiceMap.get(account.getBroker());

        return ResponseEntity.ok(ts.getPortfolio(account.getClientId(), token, withPositions));
    }


    @GetMapping("/{userId}/accounts/{account}/orders")
    public List<Order> getOrders(@PathVariable("userId") Long userId,
                                 @PathVariable("account") String accountParam,
                                 @RequestParam(value = "matches", required = false, defaultValue = "false") boolean includeMatched,
                                 @RequestParam(value = "canceled", required = false, defaultValue = "false") boolean includeCanceled,
                                 @RequestParam(value = "active", required = false, defaultValue = "true") boolean includeActive) {

        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException("Account parameters = '%s'".formatted(accountParam));
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());

        TradingService ts = tradingServiceMap.get(account.getBroker());

        return ts.getOrders(
                account.getClientId(),
                token,
                includeMatched,
                includeCanceled,
                includeActive);
    }


    @GetMapping("/{userId}/accounts/{account}/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("userId") Long userId,
                                          @PathVariable("account") String accountParam,
                                          @PathVariable("orderId") Integer orderId) {

        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException("Account parameters = '%s'".formatted(accountParam));
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());

        TradingService ts = tradingServiceMap.get(broker);

        var orders = ts.getOrders(clientId, token, true, true, true);
        var findable = orders.stream().filter(o -> o.getOrderId() == orderId).findFirst();

        return findable.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }


    @PostMapping("/{userId}/accounts/{account}/orders/order")
    public ResponseEntity<Order> newOrder(@PathVariable("userId") Long userId,
                                          @PathVariable("account") String accountParam,
                                          @RequestBody NewOrderForm form) {

        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException("Account parameters = '%s'".formatted(accountParam));
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);

        OrderCriteria criteria = OrderCriteria.builder()
                .broker(account.getBroker())
                .clientId(account.getClientId())
                .ticker(form.ticker())
                .board(Board.valueOf(form.board()))
                .quantity(form.quantity())
                .price(form.price())
                .direction(Direction.valueOf(form.direction()))
                .isMargin(false)
                .build();
        OrderCriteria.PriceType priceType = form.price() > 0 ?
                OrderCriteria.PriceType.Limit :
                OrderCriteria.PriceType.MarketPrice;
        criteria.setPriceType(priceType);

        if (form.delayTime() != null) {
            criteria.setDelayTime(form.delayTime());
            criteria.setPriceType(OrderCriteria.PriceType.Delayed);
        }
        if (form.cancelUnfulfilled()) {
            criteria.setFulFillProperty(OrderCriteria.FulFillProperty.PutInQueue);
        } else {
            criteria.setFulFillProperty(OrderCriteria.FulFillProperty.CancelUnfulfilled);
        }
        if (form.isTillCancel()) {
            criteria.setValidBefore(OrderCriteria.ValidBefore.TillCancelled);
        } else {
            criteria.setValidBefore(OrderCriteria.ValidBefore.TillEndSession);
        }
        String token = accountService.decodeToken(account.getToken());
        TradingService tradingService = tradingServiceMap.get(account.getBroker());
        Order order = tradingService.makeOrder(criteria, token);
        return ResponseEntity.ok(order);
    }


    @DeleteMapping("/{userId}/accounts/{account}/orders/{orderId}")
    public ResponseEntity<Boolean> cancelOrder(@PathVariable("userId") Long userId,
                                               @PathVariable("account") String accountParam,
                                               @PathVariable("orderId") Integer orderId) {

        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException("Account parameters = '%s'".formatted(accountParam));
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        String token = accountService.decodeToken(account.getToken());

        TradingService tradingService = tradingServiceMap.get(account.getBroker());
        tradingService.cancelOrder(clientId, token, orderId);
        return ResponseEntity.ok(true);
    }
}
