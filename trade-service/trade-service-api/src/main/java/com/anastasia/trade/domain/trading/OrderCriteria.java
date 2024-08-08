package com.anastasia.trade.domain.trading;

import com.anastasia.trade.entities.Board;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Direction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCriteria {

    private Broker broker;
    private String clientId;
    private String ticker;
    private Board board;
    private long quantity;
    private double price;
    private Direction direction;
    private PriceType priceType;
    private LocalDateTime delayTime;
    private FulFillProperty fulFillProperty;
    private ValidBefore validBefore;
    private LocalDateTime beforeTime;
    private boolean isMargin;

    public OrderCriteria() {}

    @Builder
    public OrderCriteria(Broker broker, String clientId, String ticker, Board board, long quantity, double price, Direction direction, PriceType priceType, LocalDateTime delayTime, FulFillProperty fulFillProperty, ValidBefore validBefore, LocalDateTime beforeTime, boolean isMargin) {
        this.broker = broker;
        this.clientId = clientId;
        this.ticker = ticker;
        this.board = board;
        this.quantity = quantity;
        this.price = price;
        this.direction = direction;
        this.priceType = priceType;
        this.delayTime = delayTime;
        this.fulFillProperty = fulFillProperty;
        this.validBefore = validBefore;
        this.beforeTime = beforeTime;
        this.isMargin = isMargin;
    }

    public enum PriceType {
        MarketPrice,
        Limit,
        Delayed
    }

    public enum FulFillProperty {
        PutInQueue,
        CancelUnfulfilled
    }

    public enum ValidBefore {
        TillEndSession,
        TillCancelled,
        ExactTime
    }
}
