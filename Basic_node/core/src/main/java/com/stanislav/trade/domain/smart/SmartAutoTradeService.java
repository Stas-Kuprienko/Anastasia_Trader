/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.smart;

import com.stanislav.trade.domain.smart.strategy.TradeStrategy;
import com.stanislav.trade.entities.Board;

public interface SmartAutoTradeService {

    void subscribe(String ticker, Board board, TradeStrategy strategy);
}
