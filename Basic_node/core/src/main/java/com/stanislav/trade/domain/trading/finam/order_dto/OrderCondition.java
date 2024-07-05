/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

import java.math.BigDecimal;

public record OrderCondition(Type type, BigDecimal price, String time) {

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
