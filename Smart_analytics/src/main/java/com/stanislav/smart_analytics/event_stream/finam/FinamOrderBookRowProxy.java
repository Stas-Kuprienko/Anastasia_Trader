package com.stanislav.smart_analytics.event_stream.finam;

import com.stanislav.smart_analytics.event_stream.service.OrderBookRow;
import proto.tradeapi.v1.Events;

public class FinamOrderBookRowProxy implements OrderBookRow {

    private final Events.OrderBookRow nominal;

    public FinamOrderBookRowProxy(Events.OrderBookRow nominal) {
        this.nominal = nominal;
    }

    @Override
    public double getPrice() {
        return nominal.getPrice();
    }

    @Override
    public long getQuantity() {
        return nominal.getQuantity();
    }
}
