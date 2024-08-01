/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.controller.trade;

import com.stanislav.ui.configuration.WebApplicationUIConfig;
import com.stanislav.ui.configuration.auth.form.MyUserDetails;
import com.stanislav.ui.controller.service.MVC;
import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.Direction;
import com.stanislav.ui.model.market.Futures;
import com.stanislav.ui.model.market.Stock;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.trade.OrderCriteria;
import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.service.AccountService;
import com.stanislav.ui.service.MarketDataService;
import com.stanislav.ui.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/trade")
public final class TradingController {

    public static final String STOCK_TYPE = "stock";
    public static final String FUTURES_TYPE = "futures";

    private static final String ORDERS_URI = "/orders";
    private static final String ORDER_VIEW = "order_view";
    private static final String ITEM = "item";

    private static final String resource = WebApplicationUIConfig.resource + "trade";

    private final AccountService accountService;
    private final TradingService tradingService;
    private final MarketDataService marketDataService;


    @Autowired
    public TradingController(AccountService accountService, TradingService tradingService, MarketDataService marketDataService) {
        this.tradingService = tradingService;
        this.accountService = accountService;
        this.marketDataService = marketDataService;
    }


    @GetMapping("/{account}/orders")
    public String getOrders(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("account") String accountParam,
                            @RequestParam(value = "matches", required = false, defaultValue = "false") boolean includeMatched,
                            @RequestParam(value = "canceled", required = false, defaultValue = "false") boolean includeCanceled,
                            @RequestParam(value = "active", required = false, defaultValue = "true") boolean includeActive,
                            Model model) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParam);
        }
        String broker = accountData[0];
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        var orders = tradingService.getOrders(
                userId,
                account.getBroker(),
                account.getClientId(),
                includeMatched,
                includeCanceled,
                includeActive);
        model.addAttribute("orders", orders);
        model.addAttribute("account", accountParam);
        return ORDER_VIEW;
    }

    @GetMapping("/{account}/order/{orderId}")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("account") String accountParam,
                           @PathVariable("orderId") Integer orderId,
                           Model model) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParam);
        }
        String broker = accountData[0];
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        Order order = tradingService.getOrder(userId, account.getBroker(), account.getClientId(), orderId);
        model.addAttribute("order", order);
        model.addAttribute("account", accountParam);
        return ORDER_VIEW;
    }

    @PostMapping("/{account}/order/{ticker}")
    public String newOrderHandle(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("account") String accountParam,
                                 @PathVariable("ticker") String ticker,
                                 @RequestParam("board") String board,
                                 @RequestParam("price") double price,
                                 @RequestParam("quantity") long quantity,
                                 @RequestParam("direction") String direction,
                                 @RequestParam(name = "cancelUnfulfilled", required = false, defaultValue = "false") boolean cancelUnfulfilled,
                                 @RequestParam(name = "isTillCancel", required = false, defaultValue = "false") boolean isTillCancel,
                                 @RequestParam(name = "delayTime", required = false) LocalDateTime delayTime,
                                 Model model) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParam);
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
        if (cancelUnfulfilled) {
            criteria.setFulFillProperty(OrderCriteria.FulFillProperty.PutInQueue);
        } else {
            criteria.setFulFillProperty(OrderCriteria.FulFillProperty.CancelUnfulfilled);
        }
        if (isTillCancel) {
            criteria.setValidBefore(OrderCriteria.ValidBefore.TillCancelled);
        } else {
            criteria.setValidBefore(OrderCriteria.ValidBefore.TillEndSession);
        }
        String token = accountService.decodeToken(account.getToken());
        Order order = tradingService.makeOrder(userId, criteria, token);
        model.addAttribute("order", order);
        model.addAttribute("account", accountParam);
        return MVC.REDIRECT + ORDER_VIEW;
    }

    @GetMapping("/new-order/{ticker}")
    public String newOrder(@AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("ticker") String ticker,
                           @RequestParam("type") String type, Model model) {
        List<Account> accounts = accountService.findByLogin(userDetails.getUsername());
        model.addAttribute("accounts", accounts);
        if (type.equals(STOCK_TYPE)) {
            Stock stock = marketDataService.getStock(ticker);
            model.addAttribute(ITEM, stock);
        } else if (type.equals(FUTURES_TYPE)) {
            Futures futures = marketDataService.getFutures(ticker);
            model.addAttribute(ITEM, futures);
        } else {
            log.error("type of security = " + type);
        }
        if (model.getAttribute(ITEM) == null) {
            log.info("ticker=" + ticker + " is not found");
        }
        return "new_order";
    }

    @DeleteMapping("/{account}/order/{orderId}")
    public String cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("account") String accountParam,
                              @PathVariable("orderId") Integer orderId) {
        String[] accountData = accountParam.split(":");
        if (accountData.length != 2) {
            throw new IllegalArgumentException(accountParam);
        }
        String broker = accountData[0];
        String clientId = accountData[1];
        long userId = ((MyUserDetails) userDetails).getId();
        Account account = accountService.findByClientIdAndBroker(userId, clientId, Broker.valueOf(broker));
        String token = accountService.decodeToken(account.getToken());
        tradingService.cancelOrder(userId, Broker.valueOf(broker), clientId, token, orderId);
        return MVC.REDIRECT + resource + '/' + accountParam + ORDERS_URI;
    }
}
