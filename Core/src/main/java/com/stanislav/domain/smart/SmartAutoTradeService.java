package com.stanislav.domain.smart;

import com.stanislav.domain.smart.strategy.TradeStrategy;
import com.stanislav.entities.Board;

public interface SmartAutoTradeService {

    void subscribe(String ticker, Board board, TradeStrategy strategy);
}
