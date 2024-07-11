package com.stanislav.smart.domain.trade;

import com.stanislav.smart.domain.entities.Broker;
import com.stanislav.smart.domain.entities.criteria.NewOrderCriteria;

public interface TradingService {

    int makeOrder(String clientId, String token, NewOrderCriteria newOrderCriteria);

    void checkOrder(String clientId, String token, int orderId);

    void cancelOrder(String clientId, String token, int orderId);

    Broker getBroker();
}