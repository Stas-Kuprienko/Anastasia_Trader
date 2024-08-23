package com.anastasia.smart.domain.market.order_book;

import com.anastasia.smart.domain.automation.TradeStrategy;
import io.grpc.stub.StreamObserver;
import java.util.List;

public interface OrderBookStream <ORDER_BOOK_ROW> {

    ORDER_BOOK_ROW bid();

    ORDER_BOOK_ROW ask();

    List<ORDER_BOOK_ROW> bidsList();

    List<ORDER_BOOK_ROW> asksList();

    void addListener(TradeStrategy strategy);

    void removeListener(TradeStrategy strategy);

    StreamObserver<?> getStreamObserver();
}
