package com.stanislav.domain.trading;

import com.stanislav.entities.orders.Order;
import com.stanislav.entities.orders.Stop;
import com.stanislav.entities.user.Account;

import java.util.List;

public interface TradingService {

    List<Order> getOrders(Account account, boolean matched, boolean canceled, boolean active);

    void makeOrder(Account account, Order order, TradeCriteria tradeCriteria);

    void cancelOrder(Account account, int id);

    List<Stop> getStops(Account account);

    void makeStop(Stop stop);

    void cancelStop(Account account, int id);
}
