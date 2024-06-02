/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities.markets;

import java.io.Serializable;
import java.time.LocalDate;

public interface Securities extends Serializable {

    record PriceAtTheDate(double price, LocalDate time) implements Serializable {}
}
