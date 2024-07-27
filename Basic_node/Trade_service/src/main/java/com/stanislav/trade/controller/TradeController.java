package com.stanislav.trade.controller;

import com.stanislav.trade.domain.trading.OrderCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.service.AccountService;
import com.stanislav.trade.controller.form.NewOrderForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private final AccountService accountService;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;

    @Autowired
    public TradeController(List<TradingService> tradingServices, AccountService accountService) {
        this.tradingServiceMap = new ConcurrentHashMap<>();
        for (var ts : tradingServices) {
            tradingServiceMap.put(ts.getBroker(), ts);
        }
        this.accountService = accountService;
    }


    @GetMapping("/{userId}/accounts/{account}/orders")
    public List<Order> getOrders(@PathVariable("userId") Long userId,
                                 @PathVariable("account") String accountParam,
                                 @RequestParam("matches") boolean includeMatched,
                                 @RequestParam("canceled") boolean includeCanceled,
                                 @RequestParam("active") boolean includeActive) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account parameters = " + accountParam);
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);
        TradingService tradingService;
        tradingService = tradingServiceMap.get(account.getBroker());
        String token = accountService.decodeToken(account.getToken());
        return tradingService.getOrders(
                account.getClientId(),
                token,
                includeMatched,
                includeCanceled,
                includeActive);
    }

    @GetMapping("/{userId}/accounts/{account}/orders/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("userId") Long userId,
                                          @PathVariable("account") String accountParam,
                                          @PathVariable("orderId") long orderId) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account parameters = " + accountParam);
        }
        Broker broker = Broker.valueOf(accountData[0]);
        String clientId = accountData[1];
        Account account = accountService.findByClientIdAndBroker(userId, clientId, broker);

        return ResponseEntity.ok(new Order());
    }

    @PostMapping("/{userId}/accounts/{account}/orders/order")
    public ResponseEntity<Order> newOrderHandle(@PathVariable("userId") Long userId,
                                                @PathVariable("account") String accountParam,
                                                NewOrderForm form) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account parameters = " + accountParam);
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

    @DeleteMapping("/{userId}/accounts/{account}/orders/order/{orderId}")
    public ResponseEntity<Boolean> cancelOrder(@PathVariable("userId") Long userId,
                                               @PathVariable("account") String accountParam,
                                               @PathVariable("orderId") Integer orderId) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            log.error("account parameters = " + accountParam);
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
