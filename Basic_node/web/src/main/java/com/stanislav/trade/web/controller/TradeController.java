/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.controller;

import com.stanislav.trade.domain.market.ExchangeDataProvider;
import com.stanislav.trade.domain.trading.OrderCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import com.stanislav.trade.entities.markets.Futures;
import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.web.authentication.form.MyUserDetails;
import com.stanislav.trade.web.configuration.WebApplicationConfig;
import com.stanislav.trade.web.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping("/trade")
public final class TradeController {

    public static final String STOCK_URI = "stock";
    public static final String FUTURES_URI = "futures";

    private static final String ORDER_VIEW = "order_view";
    private static final String ITEM = "item";

    private final AccountService accountService;
    private final ExchangeDataProvider exchangeDataProvider;
    private final ConcurrentHashMap<Broker, TradingService> tradingServiceMap;


    @Autowired
    public TradeController(@Qualifier("moexExchangeData") ExchangeDataProvider exchangeDataProvider,
                           List<TradingService> tradingServices, AccountService accountService) {
        this.tradingServiceMap = initTradingServiceMap(tradingServices);
        this.exchangeDataProvider = exchangeDataProvider;
        this.accountService = accountService;
    }


    @GetMapping("/orders/{clientId}")
    public String getOrders(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("clientId") String clientId,
                            @RequestParam("broker") String broker,
                            @RequestParam("matches") boolean includeMatched,
                            @RequestParam("canceled") boolean includeCanceled,
                            @RequestParam("active") boolean includeActive,
                            Model model) {
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
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
            model.addAttribute("orders", orders);
            model.addAttribute("accountId", account.getId());
            //TODO update page
            return ORDER_VIEW;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/order/{orderId}")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("orderId") long orderId,
                           @RequestParam("clientId") String clientId,
                           @RequestParam("broker") String broker, Model model) {
        //TODO
        return ORDER_VIEW;
    }

    @PostMapping("/order/{ticker}")
    public String newOrderHandle(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("ticker") String ticker,
                                 @RequestParam("account") String clientBroker,
                                 @RequestParam("board") String board,
                                 @RequestParam("price") double price,
                                 @RequestParam("quantity") long quantity,
                                 @RequestParam("direction") String direction,
                                 @RequestParam(name = "cancelUnfulfilled", required = false) Boolean cancelUnfulfilled,
                                 @RequestParam(name = "isTillCancel", required = false) Boolean isTillCancel,
                                 @RequestParam(name = "delayTime", required = false) LocalDateTime delayTime,
                                 Model model) {
        try {
            String[] accountData = clientBroker.split(":");
            if (accountData.length != 2) {
                throw new IllegalArgumentException(clientBroker);
            }
            String broker = accountData[0];
            String clientId = accountData[1];
            long userId = ((MyUserDetails) userDetails).getId();
            Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
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
            model.addAttribute("order", order);
            //TODO update page and path
            GetQueryBuilder query = new GetQueryBuilder("redirect:" + WebApplicationConfig.resource);
            query.appendToUrl("trade/order/")
                    .appendToUrl(order.getOrderId())
                    .add("clientId", account.getClientId())
                    .add("broker", broker);
            return query.build();
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/new-order/{ticker}")
    public String newOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("ticker") String ticker,
                           @RequestParam("type") String type, Model model) {
        List<Account> accounts = accountService.findByLogin(userDetails.getUsername());
        model.addAttribute("accounts", accounts);
        if (type.equals(STOCK_URI)) {
            Optional<Stock> stock = exchangeDataProvider.getStock(ticker);
            stock.ifPresent(s -> model.addAttribute(ITEM, s));
        } else if (type.equals(FUTURES_URI)) {
            Optional<Futures> futures = exchangeDataProvider.getFutures(ticker);
            futures.ifPresent(f -> model.addAttribute(ITEM, f));
        } else {
            log.error("type of security = " + type);
        }
        if (model.getAttribute(ITEM) == null) {
            log.info("ticker=" + ticker + " is not found");
        }
        return "new_order";
    }

    @DeleteMapping("/order/{orderId}")
    public String cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("orderId") Integer orderId,
                              @RequestParam("clientId") String clientId,
                              @RequestParam("broker") String broker) {

        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        String token = accountService.decodeToken(account.getToken());
        TradingService tradingService = tradingServiceMap.get(account.getBroker());
        tradingService.cancelOrder(clientId, token, orderId);

        GetQueryBuilder query = new GetQueryBuilder(
                "redirect:" + WebApplicationConfig.resource + "trade/orders/" + clientId);
        query.add("broker", broker)
                .add("matches", true)
                .add("canceled", true)
                .add("active", true);
        return query.build();
    }

    public static ConcurrentHashMap<Broker, TradingService> initTradingServiceMap(List<TradingService> tradingServices) {
        ConcurrentHashMap<Broker, TradingService> map = new ConcurrentHashMap<>();
        for (TradingService t : tradingServices) {
            map.put(t.getBroker(), t);
        }
        return map;
    }
}
