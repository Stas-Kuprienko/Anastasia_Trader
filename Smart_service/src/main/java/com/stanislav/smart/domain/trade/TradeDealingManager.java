package com.stanislav.smart.domain.trade;

import com.stanislav.smart.domain.entities.Broker;
import com.stanislav.smart.domain.entities.criteria.NewOrderCriteria;
import com.stanislav.smart.domain.trade.utils.AsyncTradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class TradeDealingManager {

    private final AsyncTradeOrder asyncTradeOrder;
    private final HashMap<Broker, TradingService> tradingServiceMap;

    @Autowired
    public TradeDealingManager(AsyncTradeOrder asyncTradeOrder, List<TradingService> tradingServices) {
        this.asyncTradeOrder = asyncTradeOrder;
        tradingServiceMap = fillInMap(tradingServices);
    }


    public void initiateNewOrder(Set<Smart.Account> accounts, NewOrderCriteria newOrderCriteria) {
        for (Smart.Account account : accounts) {
            TradingService tradingService = tradingServiceMap.get(Broker.valueOf(account.getBroker()));
            asyncTradeOrder.newOrderAsync(tradingService, account, newOrderCriteria);
        }
    }


    private HashMap<Broker, TradingService> fillInMap(List<TradingService> tradingServices) {
        HashMap<Broker, TradingService> map = new HashMap<>();
        for (var ts : tradingServices) {
            map.put(ts.getBroker(), ts);
        }
        return map;
    }
}