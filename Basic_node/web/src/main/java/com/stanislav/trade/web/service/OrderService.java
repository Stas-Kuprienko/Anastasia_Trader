package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.orders.Order;

import java.util.Optional;

public interface OrderService {

    void save(Order order);

    Optional<Order> findById(long id);

    void updateFromBroker(Order order);
}
