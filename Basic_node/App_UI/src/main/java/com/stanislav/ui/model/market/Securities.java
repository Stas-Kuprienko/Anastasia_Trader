/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.ui.model.market;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface Securities extends Serializable, Comparable<Securities> {

    record PriceAtTheDate(double price, LocalDateTime time) implements Serializable {}
}
