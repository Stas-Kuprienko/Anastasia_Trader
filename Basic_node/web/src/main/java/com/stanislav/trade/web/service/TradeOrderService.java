package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;

import java.util.List;
import java.util.Optional;

public interface TradeOrderService {

    void save(Order order);

    void caching(Order order);

    void caching(List<Order> orders);

    Optional<Order> get(long orderId);

    Optional<Order> findByOrderId(long id);

    Optional<Order> findByOrderId(long orderId, Account account);

    void updateFromBroker(Order order);
}
