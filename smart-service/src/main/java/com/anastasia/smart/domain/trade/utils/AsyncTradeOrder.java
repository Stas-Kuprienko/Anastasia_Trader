package com.anastasia.smart.domain.trade.utils;

import com.anastasia.smart.domain.trade.TradingService;
import com.anastasia.smart.entities.criteria.NewOrderCriteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.anastasia.trade.Smart;

@Component
public class AsyncTradeOrder {

    @Async
    public void newOrderAsync(TradingService ts, Smart.Account account, NewOrderCriteria criteria) {
        ts.makeOrder(account.getClientId(), account.getToken(), criteria);
    }
}
