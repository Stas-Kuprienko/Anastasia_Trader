/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Securities extends Serializable, Comparable<Long> {

    record PriceAtTheDate(double price, LocalDateTime time) implements Serializable {}
}
