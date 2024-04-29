package com.stanislav.smart_analytics.domain.analysis.candles.candles;

import java.math.BigDecimal;
import java.util.Objects;

public class Decimal {

    private final int num;
    private final byte scale;

    public Decimal(int num, int scale) {
        this.num = num;
        this.scale = (byte) scale;
    }

    public Decimal(double num) {
        BigDecimal bigDecimal = BigDecimal.valueOf(num);
        //TODO !!!!
        this.num = 0;
        this.scale = 0;
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(num, scale);
    }

    public double toDouble() {
        return Double.parseDouble(toString());
    }

    public long num() {
        return num;
    }

    public byte scale() {
        return scale;
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
        if (scale == 0) {
            return String.valueOf(num);
        }
        String value = String.valueOf(num);
        int point = value.length() - scale;
        if (point <= 0) {
            point *= -1;
            value = "0".repeat(point + 1) + value;
            point = value.length() - scale;
        }
        return value.substring(0, point) + '.' + value.substring(point);
    }
}