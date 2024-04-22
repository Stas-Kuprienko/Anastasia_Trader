package com.stanislav.domain.smart.analytics.technical;

import com.stanislav.entities.candles.Candles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class SimpleMovingAverageAide {

    private final Candles.Candle[] candles;

    private final ArrayList<BigDecimal> values;


    public SimpleMovingAverageAide(Candles candles, int period) {
        this.candles = candles.candles();
        values = new ArrayList<>();
        calculate(period);
    }


    public void calculate(int period) {
        if (period < 2 || candles.length <= period) {
            throw new IllegalArgumentException("period value is incorrect - " + period);
        }
        BigDecimal sum = BigDecimal.valueOf(0);
        BigDecimal periodDecimal = BigDecimal.valueOf(period);
        int i = 0;
        for (; i < period; i++) {
            sum = sum.add(candles[i].close().toBigDecimal());
        }
        for (; i < candles.length; i++) {
            //TODO dates !!!!!!!!!!!!
            SMAValue value = new SMAValue("", sum.divide(periodDecimal, RoundingMode.UNNECESSARY));
            values.add(value.value);
            sum = sum.subtract(candles[i - period].close().toBigDecimal());
            sum = sum.add(candles[i].close().toBigDecimal());
        }
    }

    public Candles.Candle[] getCandles() {
        return candles;
    }

    public ArrayList<BigDecimal> getValues() {
        return values;
    }

    public record SMAValue(String date, BigDecimal value) {}
}