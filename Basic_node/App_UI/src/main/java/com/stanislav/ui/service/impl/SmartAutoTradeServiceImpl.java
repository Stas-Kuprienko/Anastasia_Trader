package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.BadRequestException;
import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.TimeFrame;
import com.stanislav.ui.model.trade.SmartSubscriptionResponse;
import com.stanislav.ui.service.SmartAutoTradeService;
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
        GetRequestParametersBuilder url = new GetRequestParametersBuilder(resource);
        String account = broker.toString() + ':' + clientId;
        url.appendToUrl(userId)
                .appendToUrl("/account/")
                .appendToUrl(account)
                .appendToUrl("/subscribe/")
                .appendToUrl(strategy);
        url.add("ticker", ticker)
                .add("board", board)
                .add("time_frame", tf);
        ResponseEntity<SmartSubscriptionResponse> response = restTemplate
                .exchange(url.build(), HttpMethod.POST, new HttpEntity<>(authorization), SmartSubscriptionResponse.class);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException("Subscription request to strategy='%s' for user=%d on  is failed".formatted(strategy, userId));
        }
    }

    @Override
    public void unsubscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf) {

    }
}
