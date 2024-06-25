/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.orders.Stop;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;

import java.util.List;

public interface TradingService {

    Portfolio getPortfolio(String clientId);

    List<Order> getOrders(Account account, boolean matched, boolean canceled, boolean active);

    void makeOrder(Order order, TradeCriteria tradeCriteria);

    void cancelOrder(Account account, int orderId);

    List<Stop> getStops(Account account, boolean matched, boolean canceled, boolean active);

    void makeStop(Stop stop);

    void cancelStop(Account account, int stopId);
}
