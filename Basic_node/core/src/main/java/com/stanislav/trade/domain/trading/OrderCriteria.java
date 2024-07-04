package com.stanislav.trade.domain.trading;

import com.stanislav.trade.entities.Board;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.Direction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderCriteria implements TradeCriteria {

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
