/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.smart;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.TimeFrame;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies();

    void subscribe(String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame);

    void unsubscribe(String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame);
}
