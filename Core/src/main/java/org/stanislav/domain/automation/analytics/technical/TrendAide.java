package org.stanislav.domain.automation.analytics.technical;

import org.stanislav.entities.candles.Candles;
import org.stanislav.entities.candles.Decimal;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TrendAide {

    private final ArrayList<Decimal> maximums;
    private final ArrayList<Decimal> minimums;

    private final Candles candles;


    public TrendAide(Candles candles) {
        maximums = new ArrayList<>();
        minimums = new ArrayList<>();
        this.candles = candles;
    }

    public void upTrendAnalysis(BigDecimal min1, BigDecimal min2) {


    }


    public void addMaximum(BigDecimal value) {
        maximums.add(new Decimal(value.longValue(), value.scale()));
    }

    public void addMinimum(BigDecimal value) {
        minimums.add(new Decimal(value.longValue(), value.scale()));
    }

    public ArrayList<Decimal> getMaximums() {
        return maximums;
    }

    public ArrayList<Decimal> getMinimums() {
        return minimums;
    }

    public Candles getCandles() {
        return candles;
    }
}
