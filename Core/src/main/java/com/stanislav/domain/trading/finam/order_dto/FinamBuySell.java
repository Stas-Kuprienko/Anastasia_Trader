package com.stanislav.domain.trading.finam.order_dto;

import com.stanislav.entities.orders.Direction;
import org.springframework.lang.NonNull;

public enum FinamBuySell {

    Buy, Sell;

    public static FinamBuySell convert(@NonNull Direction direction) {
        for (FinamBuySell e : FinamBuySell.values()) {
            if (e.toString().equalsIgnoreCase(direction.toString())) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(FinamBuySell.class, direction.toString());
    }
}
