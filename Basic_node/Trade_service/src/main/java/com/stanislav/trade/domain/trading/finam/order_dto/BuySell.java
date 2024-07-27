/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam.order_dto;

import com.stanislav.trade.entities.Direction;
import org.springframework.lang.NonNull;

public enum BuySell {

    Buy, Sell;

    public static BuySell convert(@NonNull Direction direction) {
        for (BuySell e : BuySell.values()) {
            if (e.toString().equalsIgnoreCase(direction.toString())) {
                return e;
            }
        }
        throw new EnumConstantNotPresentException(BuySell.class, direction.toString());
    }
}
