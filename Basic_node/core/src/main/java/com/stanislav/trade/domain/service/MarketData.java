/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service;

import com.stanislav.trade.entities.markets.Stock;
import com.stanislav.trade.entities.user.Account;

import java.util.List;

public interface MarketData {

    Stock getStock(Account account, String ticker);

    List<Stock> getStocks(Account account);
}