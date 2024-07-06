package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.OrderDao;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.web.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("orderService")
public class OrderServiceRamImpl implements OrderService {

    private final OrderDao orderDao;
    private final ConcurrentHashMap<Long, Order> ram;


    @Autowired
    public OrderServiceRamImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
        ram = new ConcurrentHashMap<>();
    }


    @Transactional
    @Override
    public void save(Order order) {
        order = orderDao.save(order);
        ram.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(long id) {
        Optional<Order> order = Optional.ofNullable(ram.get(id));
        if (order.isEmpty()) {
            order = orderDao.findById(id);
            order.ifPresent(o -> ram.put(o.getId(), o));
        }
        return order;
    }

    @Override
    public void updateFromBroker(Order order) {

    }
}