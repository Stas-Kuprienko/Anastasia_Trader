package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.BadRequestException;
import com.stanislav.ui.exception.NotFoundException;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.forms.NewOrderForm;
import com.stanislav.ui.model.user.Portfolio;
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
    public Portfolio getPortfolio(long userId, Broker broker, String clientId, boolean withPositions) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        getRequest.appendToUrl(userId)
                .appendToUrl("/accounts")
                .appendToUrl('/')
                .appendToUrl(account)
                .appendToUrl("/portfolio");

        getRequest.add("withPositions", withPositions);

        ResponseEntity<Portfolio> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorization), Portfolio.class);

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
        StringBuilder url = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        url.append(userId)
                .append("/accounts")
                .append('/')
                .append(account)
                .append("/orders/")
                .append(orderId);

        ResponseEntity<Order> response = restTemplate
                .exchange(url.toString(), HttpMethod.GET, new HttpEntity<>(authorization), Order.class);

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

        HttpEntity<NewOrderForm> httpEntity = new HttpEntity<>(newOrderForm, authorization);
        ResponseEntity<Order> response = restTemplate
                .exchange(url.toString(), HttpMethod.POST, httpEntity, Order.class);

        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException(
                    "Order for %s with parameters: '%s' is failed".formatted(account, newOrderForm));
        }
    }

    @Override
    public boolean cancelOrder(long userId, Broker broker, String clientId, Integer orderId) {
        StringBuilder url = new StringBuilder(resource);
        String account = broker.toString() + ':' + clientId;

        url.append(userId)
                .append("/accounts/")
                .append('/')
                .append(account)
                .append("/orders/")
                .append(orderId);

        ResponseEntity<Boolean> response = restTemplate
                .exchange(url.toString(), HttpMethod.DELETE, new HttpEntity<>(authorization), Boolean.class);

        if (response.hasBody()) {
            return Boolean.TRUE.equals(response.getBody());
        } else return false;
    }
}
