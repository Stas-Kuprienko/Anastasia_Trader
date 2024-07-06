/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.market.ExchangeData;
import com.stanislav.trade.domain.trading.OrderCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.MVC;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.service.OrderService;
import com.stanislav.trade.web.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/trade")
public final class TradeController {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";
    private static final String ITEM = "item";

    private final UserDataService userDataService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final ExchangeData exchangeData;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;


    @Autowired
    public TradeController(List<TradingService> tradingServices, UserDataService userDataService,
                           AccountService accountService, OrderService orderService,
                           @Qualifier("moexExchangeData") ExchangeData exchangeData) {
        this.tradingServiceMap = initTradingServiceMap(tradingServices);
        this.userDataService = userDataService;
        this.accountService = accountService;
        this.orderService = orderService;
        this.exchangeData = exchangeData;
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
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("orderId") long id,
                           @RequestParam String clientId, Model model) {

        return "order";
    }

    @PostMapping("/order/{ticker}")
    public String newOrderHandle(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("ticker") String ticker,
                                 @RequestParam("accountId") long accountId,
                                 @RequestParam("board") String board,
                                 @RequestParam("price") double price,
                                 @RequestParam("quantity") long quantity,
                                 @RequestParam("direction") String direction,
                                 @RequestParam(name = "cancelUnfulfilled", required = false) Boolean cancelUnfulfilled,
                                 @RequestParam(name = "isTillCancel", required = false) Boolean isTillCancel,
                                 @RequestParam(name = "delayTime", required = false) LocalDateTime delayTime,
                                 Model model) {
        try {
            Account account = accountService.findById(accountId, userDetails.getUsername()).orElseThrow();
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
            OrderCriteria.PriceType priceType = price > 0 ?
                    OrderCriteria.PriceType.Limit :
                    OrderCriteria.PriceType.MarketPrice;
            criteria.setPriceType(priceType);
            if (delayTime != null) {
                criteria.setDelayTime(delayTime);
                criteria.setPriceType(OrderCriteria.PriceType.Delayed);
            }
            if (cancelUnfulfilled != null && cancelUnfulfilled) {
                criteria.setFulFillProperty(OrderCriteria.FulFillProperty.PutInQueue);
            } else {
                criteria.setFulFillProperty(OrderCriteria.FulFillProperty.CancelUnfulfilled);
            }
            if (isTillCancel != null && isTillCancel) {
                criteria.setValidBefore(OrderCriteria.ValidBefore.TillCancelled);
            } else {
                criteria.setValidBefore(OrderCriteria.ValidBefore.TillEndSession);
            }
            String token = accountService.decodeToken(account.getToken());
            TradingService tradingService = tradingServiceMap.get(account.getBroker());
            Order order = tradingService.makeOrder(criteria, token);
            order.setAccount(account);
            orderService.save(order);
            model.addAttribute("order", order);
            return MVC.REDIRECT + "/order/" + order.getId();
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException e) {
            log.warn(e.getMessage());
            return ErrorController.URL_REDIRECT + ErrorCase.BAD_REQUEST;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL_REDIRECT + ErrorCase.ACCESS_DENIED;
        }
    }

    @GetMapping("/order/{ticker}")
    public String newOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("ticker") String ticker,
                           @RequestParam("type") String type, Model model) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error("user=" + userDetails.getUsername() + " is lost");
            return "forward:login";
        }
        List<Account> accounts = user.get().getAccounts();
        model.addAttribute("accounts", accounts);
        if (type.equals(MarketController.STOCK_URI)) {
            Optional<Stock> stock = exchangeData.getStock(ticker);
            stock.ifPresent(s -> model.addAttribute(ITEM, s));
        } else if (type.equals(MarketController.FUTURES_URI)) {
            Optional<Futures> futures = exchangeData.getFutures(ticker);
            futures.ifPresent(f -> model.addAttribute(ITEM, f));
        } else {
            log.error("type of security = " + type);
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
        if (model.getAttribute(ITEM) == null) {
            log.info("ticker=" + ticker + " is not found");
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
        return "new_order";
    }


    private ConcurrentHashMap<Broker, TradingService> initTradingServiceMap(List<TradingService> tradingServices) {
        ConcurrentHashMap<Broker, TradingService> map = new ConcurrentHashMap<>();
        for (TradingService t : tradingServices) {
            map.put(t.getBroker(), t);
        }
        return map;
    }
}
