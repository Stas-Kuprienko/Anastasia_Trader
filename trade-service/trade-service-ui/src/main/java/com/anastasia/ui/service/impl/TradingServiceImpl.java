package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.exception.NotFoundException;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.forms.NewOrderForm;
import com.anastasia.ui.model.trade.Order;
import com.anastasia.ui.model.user.Portfolio;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.TradingService;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.GetRequestParametersBuilder;
import com.anastasia.ui.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("tradingService")
public class TradingServiceImpl implements TradingService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "trade/";

    private final UserService userService;
    private final MyRestClient myRestClient;


    @Autowired
    public TradingServiceImpl(UserService userService, MyRestClient myRestClient) {
        this.userService = userService;
        this.myRestClient = myRestClient;
    }


    @Override
    public Portfolio getPortfolio(long userId, Broker broker, String clientId, boolean withPositions) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        getRequest.appendToUrl(userId)
                .appendToUrl("/accounts")
                .appendToUrl('/')
                .appendToUrl(account)
                .appendToUrl("/portfolio");
        getRequest.add("withPositions", withPositions);

        User user = userService.findById(userId);
        ResponseEntity<Portfolio> response = myRestClient.get(user, getRequest.build(), Portfolio.class);

        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new NotFoundException("portfolio for account '%s' is not found".formatted(account));
        }
    }

    @Override
    public List<Order> getOrders(long userId, Broker broker, String clientId, boolean includeMatched, boolean includeCanceled, boolean includeActive) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        getRequest.appendToUrl(userId)
                .appendToUrl("/accounts")
                .appendToUrl('/')
                .appendToUrl(account)
                .appendToUrl("/orders");

        getRequest.add("includeMatched", includeMatched)
                .add("includeCanceled", includeCanceled)
                .add("includeActive", includeActive);

        User user = userService.findById(userId);
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<? extends Collection<Order>> response = myRestClient.get(user, getRequest.build(), responseType);

        if (response.hasBody()) {
            return (List<Order>) response.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Order getOrder(long userId, Broker broker, String clientId, int orderId) {
        StringBuilder url = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        url.append(userId)
                .append("/accounts")
                .append('/')
                .append(account)
                .append("/orders/")
                .append(orderId);

        User user = userService.findById(userId);
        ResponseEntity<Order> response = myRestClient.get(user, url.toString(), Order.class);

        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new NotFoundException("order=" + orderId + " is not found");
        }
    }

    @Override
    public Order makeOrder(long userId, Broker broker, String clientId, NewOrderForm newOrderForm) {
        StringBuilder url = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        url.append(userId)
                .append("/accounts")
                .append('/')
                .append(account)
                .append("/orders/")
                .append("order");

        User user = userService.findById(userId);
        ResponseEntity<Order> response = myRestClient.post(user, url.toString(), newOrderForm, Order.class);

        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException(
                    "Order for %s with parameters: '%s' is failed".formatted(account, newOrderForm));
        }
    }

    @Override
    public boolean cancelOrder(long userId, Broker broker, String clientId, int orderId) {
        StringBuilder url = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        url.append(userId)
                .append("/accounts/")
                .append('/')
                .append(account)
                .append("/orders/")
                .append(orderId);

        User user = userService.findById(userId);
        ResponseEntity<Boolean> response = myRestClient.delete(user, url.toString(), Boolean.class);

        if (response.hasBody()) {
            return Boolean.TRUE.equals(response.getBody());
        } else return false;
    }
}
