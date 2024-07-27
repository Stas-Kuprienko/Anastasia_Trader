package com.stanislav.ui.service;

import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.TimeFrame;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies();

    void subscribe(String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf, String token);

    void unsubscribe(String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf, String token);
}
