package com.anastasia.smart.domain.trade;

import com.anastasia.smart.entities.Broker;
import com.anastasia.smart.entities.criteria.NewOrderCriteria;

public interface TradingService {

    int makeOrder(String clientId, String token, NewOrderCriteria newOrderCriteria);

    void checkOrder(String clientId, String token, int orderId);

    void cancelOrder(String clientId, String token, int orderId);

    Broker getBroker();
}