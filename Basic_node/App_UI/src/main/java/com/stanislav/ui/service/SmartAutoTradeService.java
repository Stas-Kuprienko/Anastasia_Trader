package com.stanislav.ui.service;

import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.TimeFrame;
import com.stanislav.ui.model.trade.SmartSubscriptionResponse;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies();

    SmartSubscriptionResponse subscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf);

    SmartSubscriptionResponse unsubscribe(long userId, String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf);
}
