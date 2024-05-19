/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import java.time.LocalDateTime;

public interface Securities {

    record PriceAtTheTime(double price, LocalDateTime time) {}
}
