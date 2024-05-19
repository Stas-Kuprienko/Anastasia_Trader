/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.entities;

import java.math.BigDecimal;
import java.util.Objects;

public record Decimal(long num, int scale) {

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(num, scale);
    }

    public double toDouble() {
        return Double.parseDouble(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decimal decimal = (Decimal) o;
        return num == decimal.num && scale == decimal.scale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toDouble());
    }

    @Override
    public String toString() {
        return BigDecimal.valueOf(num, scale).toString();
    }
}