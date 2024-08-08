/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.smart;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.TimeFrame;

import java.util.Set;

public interface SmartAutoTradeService {

    Set<String> getStrategies();

    void subscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token);

    void unsubscribe(String clientId, Broker broker, String ticker, Board board, String strategyName, TimeFrame.Scope timeFrame, String token);
}
