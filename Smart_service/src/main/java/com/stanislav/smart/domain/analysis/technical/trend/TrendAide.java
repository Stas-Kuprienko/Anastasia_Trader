/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.analysis.technical.trend;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.candles.PriceCandleBox;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TrendAide implements AnalysisAide {

    private final ArrayList<BigDecimal> maximums;
    private final ArrayList<BigDecimal> minimums;

    private final PriceCandleBox priceCandleBox;


    TrendAide(PriceCandleBox priceCandleBox) {
        maximums = new ArrayList<>();
        minimums = new ArrayList<>();
        this.priceCandleBox = priceCandleBox;
    }

    public void upTrendAnalysis(BigDecimal min1, BigDecimal min2) {


    }


    public void addMaximum(BigDecimal value) {
        maximums.add(value);
    }

    public void addMinimum(BigDecimal value) {
        minimums.add(value);
    }

    public ArrayList<BigDecimal> getMaximums() {
        return maximums;
    }

    public ArrayList<BigDecimal> getMinimums() {
        return minimums;
    }

    public PriceCandleBox getCandles() {
        return priceCandleBox;
    }
}
