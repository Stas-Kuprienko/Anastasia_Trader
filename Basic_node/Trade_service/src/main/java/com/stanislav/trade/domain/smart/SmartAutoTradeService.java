/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.smart;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.TimeFrame;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies();

    void subscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token);

    void unsubscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token);
}
