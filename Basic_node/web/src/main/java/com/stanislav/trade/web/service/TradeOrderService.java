package com.stanislav.trade.web.service;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;

import java.util.Optional;

public interface TradeOrderService {

    void save(Order order);

    Optional<Order> findById(long id);

    Optional<Order> findByOrderId(int orderId, Account account);

    void updateFromBroker(Order order);
}
