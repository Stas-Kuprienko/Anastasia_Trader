package com.stanislav.trade.web.service.ram;

import com.stanislav.trade.datasource.TradeOrderDao;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.web.service.TradeOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service("orderService")
public class TradeOrderServiceRamImpl implements TradeOrderService {

    private final TradeOrderDao tradeOrderDao;
    private final ConcurrentHashMap<Long, Order> ram;


    @Autowired
    public TradeOrderServiceRamImpl(TradeOrderDao tradeOrderDao) {
        this.tradeOrderDao = tradeOrderDao;
        ram = new ConcurrentHashMap<>();
    }


    @Transactional
    @Override
    public void save(Order order) {
        order = tradeOrderDao.save(order);
        ram.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(long id) {
        Optional<Order> order = Optional.ofNullable(ram.get(id));
        if (order.isEmpty()) {
            order = tradeOrderDao.findById(id);
            order.ifPresent(o -> ram.put(o.getId(), o));
        }
        return order;
    }

    @Override
    public Optional<Order> findByOrderId(int orderId, Account account) {
        return tradeOrderDao.findByOrderIdAndAccountId(orderId, account);
    }

    @Override
    public void updateFromBroker(Order order) {

    }
}