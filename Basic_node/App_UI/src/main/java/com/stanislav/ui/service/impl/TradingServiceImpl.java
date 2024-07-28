package com.stanislav.ui.service.impl;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.trade.Order;
import com.stanislav.ui.model.trade.OrderCriteria;
import com.stanislav.ui.service.TradingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tradingService")
public class TradingServiceImpl implements TradingService {

    @Override
    public List<Order> getOrder(Broker broker, String clientId, String token, boolean includeMatched, boolean includeCanceled, boolean includeActive) {
        return null;
    }

    @Override
    public Order getOrder(Broker broker, String clientId, String token, Integer orderId) {
        return null;
    }

    @Override
    public Order makeOrder(OrderCriteria criteria, String token) {
        return null;
    }

    @Override
    public void cancelOrder(Broker broker, String clientId, String token, Integer orderId) {

    }
}
