package com.stanislav.ui.service;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.trade.OrderCriteria;

import java.util.List;

public interface TradingService {

    List<Order> getOrder(Broker broker, String clientId, String token, boolean includeMatched, boolean includeCanceled, boolean includeActive);

    Order getOrder(Broker broker, String clientId, String token, Integer orderId);

    Order makeOrder(OrderCriteria criteria, String token);

    void cancelOrder(Broker broker, String clientId, String token, Integer orderId);
}
