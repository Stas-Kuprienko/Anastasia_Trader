package com.stanislav.entities.candles;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public record Decimal(long num, int scale) {

    public BigDecimal toBigDecimal() {
        return new BigDecimal(BigInteger.valueOf(num), scale);
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
        return Objects.hash(num, scale);
    }

    @Override
    public String toString() {
        return "Decimal{" +
                "num=" + num +
                ", scale=" + scale +
                '}';
    }
}
