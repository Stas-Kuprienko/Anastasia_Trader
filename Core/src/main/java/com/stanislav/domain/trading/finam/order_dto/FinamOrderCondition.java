package com.stanislav.domain.trading.finam.order_dto;

import java.math.BigDecimal;

public record FinamOrderCondition (Type type, BigDecimal price, String time) {

    public enum Type {
        Bid,
        BidOrLast,
        Ask,
        AskOrLast,
        Time,
        CovDown,
        CovUp,
        LastUp,
        LastDown
    }
}
