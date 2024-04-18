package com.stanislav.event_stream;

public interface OrderBookStreamService {

    void subscribe(String board, String ticker) throws InterruptedException;

    void unsubscribe(String board, String ticker);

    OrderBookRow currentAsk();

    OrderBookRow currentBid();
}
