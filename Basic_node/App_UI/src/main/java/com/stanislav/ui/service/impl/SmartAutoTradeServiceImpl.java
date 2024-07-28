package com.stanislav.ui.service.impl;

import com.stanislav.ui.model.Board;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.TimeFrame;
import com.stanislav.ui.service.SmartAutoTradeService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("smartAutoTradeService")
public class SmartAutoTradeServiceImpl implements SmartAutoTradeService {

    @Override
    public Set<String> getStrategies() {
        return null;
    }

    @Override
    public void subscribe(String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf, String token) {

    }

    @Override
    public void unsubscribe(String clientId, Broker broker, String ticker, Board board, String strategy, TimeFrame.Scope tf, String token) {

    }
}
