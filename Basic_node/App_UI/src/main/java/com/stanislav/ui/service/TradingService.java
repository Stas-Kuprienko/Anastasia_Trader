package com.stanislav.ui.service;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.trade.OrderCriteria;

import java.util.List;

public interface TradingService {

    List<Order> getOrders(long userId, Broker broker, String clientId, boolean includeMatched, boolean includeCanceled, boolean includeActive);

    Order getOrder(long userId, Broker broker, String clientId, Integer orderId);

    Order makeOrder(long userId, OrderCriteria criteria, String token);

    void cancelOrder(long userId, Broker broker, String clientId, String token, Integer orderId);
}
