package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.web.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("orderService")
public class TradeOrderServiceRamImpl implements TradeOrderService {

//    private final TradeOrderDao tradeOrderDao;
    private final ConcurrentHashMap<Long, Order> ram;


    @Autowired
    public TradeOrderServiceRamImpl() {
//        this.tradeOrderDao = tradeOrderDao;
        ram = new ConcurrentHashMap<>();
    }


//    @Transactional
    @Override
    public void save(Order order) {
//        order = tradeOrderDao.save(order);
        ram.put(order.getOrderId(), order);
    }

    @Override
    public void caching(Order order) {
        ram.put(order.getOrderId(), order);
    }

    @Override
    public void caching(List<Order> orders) {
        for (Order o : orders) {
            ram.put(o.getOrderId(), o);
        }
    }

    @Override
    public Optional<Order> get(long orderId) {
        return Optional.ofNullable(ram.get(orderId));
    }

    @Override
    public Optional<Order> findByOrderId(long orderId) {
        Optional<Order> order = Optional.ofNullable(ram.get(orderId));
        if (order.isEmpty()) {
//            order = tradeOrderDao.findById(orderId);
            order.ifPresent(o -> ram.put(o.getOrderId(), o));
        }
        return order;
    }

    @Override
    public Optional<Order> findByOrderId(long orderId, Account account) {
        return Optional.ofNullable(ram.get(orderId));
//        return tradeOrderDao.findByOrderIdAndAccountId(orderId, account);
    }

    @Override
    public void updateFromBroker(Order order) {

    }
}