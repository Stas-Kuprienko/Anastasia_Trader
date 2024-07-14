package com.stanislav.smart.domain.trade.utils;

import com.stanislav.smart.domain.entities.criteria.NewOrderCriteria;
import com.stanislav.smart.domain.trade.TradingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import stanislav.anastasia.trade.Smart;

@Component
public class AsyncTradeOrder {

    @Async
    public void newOrderAsync(TradingService ts, Smart.Account account, NewOrderCriteria criteria) {
        ts.makeOrder(account.getClientId(), account.getToken(), criteria);
    }
}
