package org.stanislav.domain.trading.finam.order_dto;

import org.springframework.lang.NonNull;
import org.stanislav.entities.orders.Direction;

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
