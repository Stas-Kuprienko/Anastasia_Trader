package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.web.service.OrderService;
import org.springframework.stereotype.Service;


@Service("orderService")
public class OrderServiceRamImpl implements OrderService {

    @Override
    public void save(Order order) {

    }

    @Override
    public void updateFromBroker(Order order) {

    }
}
