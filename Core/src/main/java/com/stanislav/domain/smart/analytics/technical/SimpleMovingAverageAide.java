package com.stanislav.domain.smart.analytics.technical;

import com.stanislav.entities.candles.Candles;

import java.math.BigDecimal;
import java.math.BigInteger;
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
        long sum = 0;
        int i = 0;
        for (; i <= period; i++) {
            sum += candles[i].close().num();
        }
        int scale = candles[0].open().scale();
        for (; i < candles.length; i++) {
            values.add(new BigDecimal(BigInteger.valueOf(sum/period), scale));
            sum -= candles[i - period].close().num();
            sum += candles[i].close().num();
        }

    }

    public Candles.Candle[] getCandles() {
        return candles;
    }

    public ArrayList<BigDecimal> getValues() {
        return values;
    }
}
