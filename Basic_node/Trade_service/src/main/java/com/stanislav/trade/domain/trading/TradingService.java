/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading;

import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.orders.Stop;
import com.stanislav.trade.entities.user.Portfolio;

import java.util.List;

public interface TradingService {

    Broker getBroker();

    Portfolio getPortfolio(String clientId, String token, boolean withPositions);

    List<Order> getOrders(String clientId, String token, boolean matched, boolean canceled, boolean active);

    Order makeOrder(OrderCriteria criteria, String token);

    void cancelOrder(String clientId, String token, int orderId);

    List<Stop> getStops(String clientId, String token, boolean matched, boolean canceled, boolean active);

    void makeStop(Stop stop);

    void cancelStop(String clientId, String token, int stopId);
}