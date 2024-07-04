package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.orders.Order;

public interface OrderService {

    void save(Order order);

    void updateFromBroker(Order order);
}
