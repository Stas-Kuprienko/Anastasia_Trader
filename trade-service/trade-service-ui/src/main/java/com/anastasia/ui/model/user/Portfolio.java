package com.anastasia.ui.model.user;

import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.Market;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public record Portfolio(String clientId, Broker broker, BigDecimal balance, List<Position> positions) {

    public record Position(String ticker, Market market, double price, double totalCost, long quantity, Currency currency, double profit) {}
}
