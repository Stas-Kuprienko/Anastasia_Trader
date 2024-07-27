/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

public record OrderValidBefore(Type type, String time) {

    public enum Type {
        TillEndSession,
        TillCancelled,
        ExactTime
    }
}