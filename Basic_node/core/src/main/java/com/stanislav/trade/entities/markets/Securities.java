/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import java.time.LocalDate;

public interface Securities {

    record PriceAtTheDate(double price, LocalDate time) {}
}
