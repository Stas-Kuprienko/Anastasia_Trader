package com.anastasia.ui.service.impl;

import com.anastasia.ui.utils.GetRequestParametersBuilder;
import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.configuration.auth.TokenAuthService;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.model.Board;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.TimeFrame;
import com.anastasia.ui.model.trade.SmartSubscriptionResponse;
import com.anastasia.ui.service.SmartAutoTradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Set;

@Service("smartAutoTradeService")
public class SmartAutoTradeServiceImpl implements SmartAutoTradeService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "smart/";

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;


    @Autowired
    public SmartAutoTradeServiceImpl(RestTemplate restTemplate, TokenAuthService authService) {
        this.restTemplate = restTemplate;
        this.authorization = authService.authorize();
    }


    @Override
    public Set<String> getStrategies() {
        ParameterizedTypeReference<Set<String>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Set<String>> response = restTemplate
                .exchange(responseType + "strategies", HttpMethod.GET, new HttpEntity<>(authorization), responseType);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public SmartSubscriptionResponse subscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf) {
        ResponseEntity<SmartSubscriptionResponse> response = doRequest(RequestType.SUBSCRIBE, userId, clientId, broker, ticker, board, strategy, tf);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException("Subscription request to strategy='%s' for user=%d on  is failed".formatted(strategy, userId));
        }
    }

    @Override
    public SmartSubscriptionResponse unsubscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf) {
        ResponseEntity<SmartSubscriptionResponse> response = doRequest(RequestType.UNSUBSCRIBE, userId, clientId, broker, ticker, board, strategy, tf);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException("Unsubscription request from strategy='%s' for user=%d on  is failed".formatted(strategy, userId));
        }
    }

    private ResponseEntity<SmartSubscriptionResponse> doRequest(RequestType requestType, long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf) {
        GetRequestParametersBuilder url = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;
        url.appendToUrl(userId)
                .appendToUrl("/account/")
                .appendToUrl(account)
                .appendToUrl(requestType.value)
                .appendToUrl(strategy);
        url.add("ticker", ticker)
                .add("board", board)
                .add("time_frame", tf);
        return restTemplate
                .exchange(url.build(), HttpMethod.POST, new HttpEntity<>(authorization), SmartSubscriptionResponse.class);
    }

    enum RequestType {

        SUBSCRIBE("/subscribe/"),
        UNSUBSCRIBE("/subscribe/");

        final String value;

        RequestType(String value) {
            this.value = value;
        }
    }
}