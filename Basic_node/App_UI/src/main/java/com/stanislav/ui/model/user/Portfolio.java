package com.stanislav.ui.model.user;

import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.Market;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public record Portfolio(String clientId, Broker broker, BigDecimal balance, List<Position> positions) {

    public record Position(String ticker, Market market, double price, double totalCost, long quantity, Currency currency, double profit) {}
}
