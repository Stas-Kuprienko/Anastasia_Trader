package com.stanislav.ui.service;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.forms.NewOrderForm;
import com.stanislav.ui.model.user.Portfolio;

import java.util.List;

public interface TradingService {

    Portfolio getPortfolio(long userId, Broker broker, String clientId, boolean withPositions);

    List<Order> getOrders(long userId, Broker broker, String clientId, boolean includeMatched, boolean includeCanceled, boolean includeActive);

    Order getOrder(long userId, Broker broker, String clientId, Integer orderId);

    Order makeOrder(long userId, Broker broker, String clientId, NewOrderForm newOrderForm);

    boolean cancelOrder(long userId, Broker broker, String clientId, Integer orderId);
}
