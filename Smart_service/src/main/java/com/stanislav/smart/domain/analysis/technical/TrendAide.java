package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.candles.Candles;
import com.stanislav.smart.domain.entities.Decimal;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TrendAide implements AnalysisAide {

    private final ArrayList<Decimal> maximums;
    private final ArrayList<Decimal> minimums;

    private final Candles candles;


    TrendAide(Candles candles) {
        maximums = new ArrayList<>();
        minimums = new ArrayList<>();
        this.candles = candles;
    }

    public void upTrendAnalysis(BigDecimal min1, BigDecimal min2) {


    }


    public void addMaximum(BigDecimal value) {
        maximums.add(new Decimal(value.intValue(), value.scale()));
    }

    public void addMinimum(BigDecimal value) {
        minimums.add(new Decimal(value.intValue(), value.scale()));
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
