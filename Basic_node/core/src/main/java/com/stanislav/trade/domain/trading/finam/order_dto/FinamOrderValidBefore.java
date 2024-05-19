/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

public record FinamOrderValidBefore (Type type, String time) {

    public enum Type {
        TillEndSession,
        TillCancelled,
        ExactTime
    }
}