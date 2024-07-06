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
import com.stanislav.trade.web.configuration.WebApplicationConfig;
import com.stanislav.trade.web.controller.service.ErrorController;
import com.stanislav.trade.web.controller.service.MVC;
import com.stanislav.trade.web.service.AccountService;
import com.stanislav.trade.web.controller.service.ErrorCase;
import com.stanislav.trade.web.service.TradeOrderService;
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

    private static final String ORDER_VIEW = "order_view";
    private static final String ITEM = "item";

    private final UserDataService userDataService;
    private final AccountService accountService;
    private final TradeOrderService tradeOrderService;
    private final ExchangeData exchangeData;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;


    @Autowired
    public TradeController(List<TradingService> tradingServices, UserDataService userDataService,
                           AccountService accountService, TradeOrderService tradeOrderService,
                           @Qualifier("moexExchangeData") ExchangeData exchangeData) {
        this.tradingServiceMap = initTradingServiceMap(tradingServices);
        this.userDataService = userDataService;
        this.accountService = accountService;
        this.tradeOrderService = tradeOrderService;
        this.exchangeData = exchangeData;
    }


    @GetMapping("/orders/{clientId}")
    public String getOrders(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("clientId") String clientId,
                            @RequestParam("matches") boolean includeMatched,
                            @RequestParam("canceled") boolean includeCanceled,
                            @RequestParam("active") boolean includeActive,
                            Model model) {
        Optional<User> user = userDataService.findByLogin(userDetails.getUsername());
        if (user.isEmpty()) {
            log.error(userDetails.getUsername() + " is not found");
            return "login";
        }
        Optional<Account> optionalAccount = accountService.findByClientId(clientId, user.get());
        if (optionalAccount.isEmpty()) {
            log.info("account=" + clientId + " is not found");
            return ErrorController.URL_FORWARD + ErrorCase.NOT_FOUND;
        }
        Account account = optionalAccount.get();
        try {
            TradingService tradingService;
            tradingService = tradingServiceMap.get(account.getBroker());
            String token = accountService.decodeToken(account.getToken());
            var orders = tradingService.getOrders(
                    account.getClientId(),
                    token,
                    includeMatched,
                    includeCanceled,
                    includeActive);
            tradeOrderService.caching(orders);
            model.addAttribute("orders", orders);
            model.addAttribute("accountId", account.getId());
            return ORDER_VIEW;
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error(e.getMessage());
            return ErrorController.URL_FORWARD + ErrorCase.BAD_REQUEST;
        }
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("orderId") long orderId,
                           @RequestParam("accountId") long accountId, Model model) {
        try {
            Optional<Account> account = accountService.findById(accountId, userDetails.getUsername());
            if (account.isPresent()) {
                Optional<Order> order = tradeOrderService.findByOrderId(orderId, account.get());
                if (order.isEmpty()) {
                    Account a = account.get();
                    TradingService tradingService = tradingServiceMap.get(a.getBroker());
                    List<Order> orders = tradingService.getOrders(
                            a.getClientId(),
                            accountService.decodeToken(a.getToken()),
                            true, true, true);
                    order = orders.stream()
                            .filter(o -> o.getOrderId().equals(orderId) && o.getClientId().equals(a.getClientId()))
                            .findFirst();
                }
                if (order.isPresent()) {
                    model.addAttribute("order", order.get());
                    return ORDER_VIEW;
                }
            }
            return ErrorController.URL_FORWARD + ErrorCase.NOT_FOUND;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL_FORWARD + ErrorCase.ACCESS_DENIED;
        }
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
            tradeOrderService.caching(order);
            model.addAttribute("order", order);
            //TODO safe
            String path = "trade/order/" + order.getOrderId() + "?accountId=" + account.getId();
            return MVC.REDIRECT + WebApplicationConfig.resource + path;
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException e) {
            log.warn(e.getMessage());
            return ErrorController.URL_REDIRECT + ErrorCase.BAD_REQUEST;
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage());
            return ErrorController.URL_REDIRECT + ErrorCase.ACCESS_DENIED;
        }
    }

    @GetMapping("/new-order/{ticker}")
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
