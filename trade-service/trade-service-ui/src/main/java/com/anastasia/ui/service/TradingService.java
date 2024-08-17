package com.anastasia.ui.service;

import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.trade.Order;
import com.anastasia.ui.model.forms.NewOrderForm;
import com.anastasia.ui.model.user.Portfolio;

import java.util.List;

public interface TradingService {

    Portfolio getPortfolio(long userId, Broker broker, String clientId, boolean withPositions);

    List<Order> getOrders(long userId, Broker broker, String clientId, boolean includeMatched, boolean includeCanceled, boolean includeActive);

    Order getOrder(long userId, Broker broker, String clientId, int orderId);

    Order makeOrder(long userId, Broker broker, String clientId, NewOrderForm newOrderForm);

    boolean cancelOrder(long userId, Broker broker, String clientId, int orderId);
}
