package com.anastasia.ui.service;

import com.anastasia.ui.model.Board;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.TimeFrame;
import com.anastasia.ui.model.trade.SmartSubscriptionResponse;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies(long userId);

    SmartSubscriptionResponse subscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf);

    SmartSubscriptionResponse unsubscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf);
}
