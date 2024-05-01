package com.stanislav.domain.trading;

import com.stanislav.entities.markets.Stock;
import com.stanislav.entities.user.Account;

import java.util.List;

public interface MarketData {

    Stock getStock(Account account, String ticker);

    List<Stock> getStocks(Account account);
}