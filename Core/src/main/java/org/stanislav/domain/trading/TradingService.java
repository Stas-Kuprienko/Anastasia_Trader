package org.stanislav.domain.trading;

import org.stanislav.entities.orders.Order;
import org.stanislav.entities.orders.Stop;
import org.stanislav.entities.user.Account;

import java.util.List;

public interface TradingService {

    List<Order> getOrders(Account account, boolean matched, boolean canceled, boolean active);

    void makeOrder(Account account, Order order);

    void makeOrder(Account account, Order order, boolean useCredit);

    void cancelOrder(Account account, int id);

    List<Stop> getStops(Account account);

    void makeStop(Stop stop);

    void cancelStop(Account account, int id);
}
