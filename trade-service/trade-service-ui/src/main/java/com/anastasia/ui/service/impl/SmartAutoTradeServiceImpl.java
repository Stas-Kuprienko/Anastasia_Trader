package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.model.Board;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.TimeFrame;
import com.anastasia.ui.model.trade.SmartSubscriptionResponse;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.SmartAutoTradeService;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.GetRequestParametersBuilder;
import com.anastasia.ui.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Service("smartAutoTradeService")
public class SmartAutoTradeServiceImpl implements SmartAutoTradeService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "smart/";

    private final UserService userService;
    private final MyRestClient myRestClient;


    @Autowired
    public SmartAutoTradeServiceImpl(UserService userService, MyRestClient myRestClient) {
        this.userService = userService;
        this.myRestClient = myRestClient;
    }


    @Override
    public Set<String> getStrategies(long userId) {
        User user = userService.findById(userId);
        ParameterizedTypeReference<Set<String>> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<? extends Collection<String>> response =
                myRestClient.get(user, resource + "strategies", responseType);

        if (response.hasBody()) {
            return (Set<String>) response.getBody();
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

        User user = userService.findById(userId);
        return myRestClient.post(user, url.build(), SmartSubscriptionResponse.class);
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