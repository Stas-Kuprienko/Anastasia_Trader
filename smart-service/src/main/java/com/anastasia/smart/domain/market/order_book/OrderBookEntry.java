package com.anastasia.smart.domain.market.order_book;

import com.anastasia.smart.exceptions.EventStreamException;
import proto.tradeapi.v1.Events;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBookEntry {

    private final Lock lock;
    private Events.OrderBookEvent orderBook;
    private boolean isActive;

    public OrderBookEntry() {
        lock = new ReentrantLock();
    }

    public Events.OrderBookEvent getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(Events.Event event) {
        if (event.getPayloadCase().getNumber() == 3) {
            lock.lock();
            orderBook = event.getOrderBook();
            isActive = true;
            lock.unlock();
        } else {
            if (!event.getResponse().getSuccess()) {
                isActive = false;
                throw new EventStreamException(event.getResponse().getErrorsList());
            }
        }
    }

    public boolean isEmpty() {
        return orderBook == null ||
                orderBook.getAsksList().isEmpty() &&
                        orderBook.getBidsList().isEmpty();
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
