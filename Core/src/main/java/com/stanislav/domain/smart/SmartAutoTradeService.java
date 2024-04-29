package com.stanislav.domain.smart;

import com.stanislav.domain.smart.strategy.TradeStrategy;
import com.stanislav.entities.user.Account;

public interface SmartAutoTradeService {

    void subscribe(Account account, String security, double price, int quantity, TradeStrategy strategy);
}
