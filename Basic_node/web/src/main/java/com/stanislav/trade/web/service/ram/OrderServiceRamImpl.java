package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.OrderDao;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.web.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service("orderService")
public class OrderServiceRamImpl implements OrderService {

//    private OrderDao orderDao;
    private final ConcurrentHashMap<Long, Order> ordersMap;

    public OrderServiceRamImpl() {
        ordersMap = new ConcurrentHashMap<>();
    }


    @Override
    public void save(Order order) {

    }

    @Override
    public void updateFromBroker(Order order) {

    }
}
