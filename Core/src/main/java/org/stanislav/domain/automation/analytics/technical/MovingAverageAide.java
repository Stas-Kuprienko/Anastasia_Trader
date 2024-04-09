package org.stanislav.domain.automation.analytics.technical;

import org.stanislav.entities.candles.Candles;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MovingAverageAide {

    private final Candles candles;

    private final ArrayList<BigDecimal> values;


    public MovingAverageAide(Candles candles) {
        this.candles = candles;
        values = new ArrayList<>();
    }


    public void calculate(int valuesNumber) {


    }

    public Candles getCandles() {
        return candles;
    }

    public ArrayList<BigDecimal> getValues() {
        return values;
    }
}
