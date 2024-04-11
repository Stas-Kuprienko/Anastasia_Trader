package com.stanislav.domain.trading.finam.order_dto;

public record FInamOrderValidBefore(Type type, String time) {

    public enum Type {
        TillEndSession,
        TillCancelled,
        ExactTime
    }
}
