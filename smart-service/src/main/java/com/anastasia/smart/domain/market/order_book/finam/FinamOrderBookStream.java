package com.anastasia.smart.domain.market.order_book.finam;

import com.anastasia.smart.domain.automation.TradeStrategy;
import com.anastasia.smart.domain.market.order_book.OrderBookStream;
import com.anastasia.smart.domain.utils.StreamObserverDriver;
import io.grpc.stub.StreamObserver;
import proto.tradeapi.v1.Events;
import java.util.EmptyStackException;
import java.util.List;

public class FinamOrderBookStream implements OrderBookStream<Events.OrderBookRow> {

    private final FinamOrderBookStreamObserver streamObserver;
    private final FinamOrderBookEntry entry;
    private final StreamObserverDriver<TradeStrategy> driver;

    public FinamOrderBookStream(FinamOrderBookStreamObserver streamObserver, StreamObserverDriver<TradeStrategy> driver) {
        this.streamObserver = streamObserver;
        entry = streamObserver.entry();
        this.driver = driver;
    }

    @Override
    public Events.OrderBookRow bid() {
        if (entry.isActive() && !streamObserver.entry().isEmpty()) {
            return entry.getOrderBook().getBids(0);
        } else {
            throw new EmptyStackException();
        }
    }

    @Override
    public Events.OrderBookRow ask() {
        if (entry.isActive() && !streamObserver.entry().isEmpty()) {
            return entry.getOrderBook().getAsks(0);
        } else {
            throw new EmptyStackException();
        }
    }

    @Override
    public List<Events.OrderBookRow> bidsList() {
        if (entry.isActive() && !streamObserver.entry().isEmpty()) {
            return entry.getOrderBook().getBidsList();
        } else {
            throw new EmptyStackException();
        }
    }

    @Override
    public List<Events.OrderBookRow> asksList() {
        if (entry.isActive() && !streamObserver.entry().isEmpty()) {
            return entry.getOrderBook().getAsksList();
        } else {
            throw new EmptyStackException();
        }
    }

    @Override
    public void addListener(TradeStrategy strategy) {
        if (driver.isDisabled()) {
            driver.start();
        }
        driver.addListener(strategy);
    }

    @Override
    public void removeListener(TradeStrategy strategy) {
        driver.removeListener(strategy);
    }

    @Override
    public StreamObserver<Events.Event> getStreamObserver() {
        return streamObserver;
    }
}