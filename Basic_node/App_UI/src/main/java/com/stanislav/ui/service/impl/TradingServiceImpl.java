package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.NotFoundException;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.trade.OrderCriteria;
import com.stanislav.ui.service.TradingService;
import com.stanislav.ui.utils.GetRequestParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service("tradingService")
public class TradingServiceImpl implements TradingService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "trade/";

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;

    @Autowired
    public TradingServiceImpl(RestTemplate restTemplate, TokenAuthService authService) {
        this.restTemplate = restTemplate;
        authorization = authService.authorize();
    }


    @Override
    public List<Order> getOrders(long userId, Broker broker, String clientId, boolean includeMatched, boolean includeCanceled, boolean includeActive) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        getRequest.appendToUrl(userId)
                .appendToUrl("/accounts/")
                .appendToUrl(account)
                .appendToUrl("/orders");

        getRequest.add("includeMatched", includeMatched)
                .add("includeCanceled", includeCanceled)
                .add("includeActive", includeActive);

        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Order>> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorization), responseType);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Order getOrder(long userId, Broker broker, String clientId, Integer orderId) {
        StringBuilder str = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;
        str.append(userId)
                .append("/accounts/")
                .append(account)
                .append("/orders/")
                .append(orderId);
        ResponseEntity<Order> order = restTemplate
                .exchange(str.toString(), HttpMethod.GET, new HttpEntity<>(authorization), Order.class);
        if (order.hasBody()) {
            return order.getBody();
        } else {
            throw new NotFoundException("order=" + orderId + " is not found");
        }
    }

    @Override
    public Order makeOrder(long userId, OrderCriteria criteria, String token) {
        return null;
    }

    @Override
    public void cancelOrder(long userId, Broker broker, String clientId, String token, Integer orderId) {

    }
}
