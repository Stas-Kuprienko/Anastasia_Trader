package com.stanislav.smart.domain.trade;

import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.Direction;

public interface TradingService {

    void makeOrder(String clientId, String token, String ticker, Board board, double price, long quantity, Direction direction);

    void checkOrder(String clientId, String token, int orderId);

    void cancelOrder(String clientId, String token, int orderId);
}