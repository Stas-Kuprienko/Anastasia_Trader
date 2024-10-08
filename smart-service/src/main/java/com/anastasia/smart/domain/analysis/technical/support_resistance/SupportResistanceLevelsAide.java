/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.domain.analysis.technical.support_resistance;

import com.anastasia.smart.domain.analysis.AnalysisAide;
import com.anastasia.smart.entities.TimeFrame;
import com.anastasia.smart.entities.candles.PriceCandleBox;
import com.anastasia.trade.Smart;
import java.math.BigDecimal;
import java.util.HashMap;

public class SupportResistanceLevelsAide implements AnalysisAide {

    private final Smart.Security security;
    private final PriceCandleBox priceCandleBox;
    private final TimeFrame.Scope timeFrame;
    private final HashMap<PriceRange, Level> levels;


    SupportResistanceLevelsAide(Smart.Security security, PriceCandleBox priceCandleBox, TimeFrame.Scope timeFrame) {
        this.security = security;
        this.priceCandleBox = priceCandleBox;
        this.timeFrame = timeFrame;
        this.levels = new HashMap<>();
        calculate();
    }

    public void calculate() {

    }

    public void addLevel(BigDecimal from, BigDecimal to) {

    }

    public Smart.Security getSecurity() {
        return security;
    }

    public PriceCandleBox getCandles() {
        return priceCandleBox;
    }

    public HashMap<PriceRange, Level> getLevels() {
        return levels;
    }


    public record PriceRange (BigDecimal from, BigDecimal to) {

    }

    /**
     * The description of support/resistance line parameters.
     * @param timeFrame The timeFrame of the level.
     * @param averageVolume The average value of trading volumes around the price range of the level.
     * @param importance The index of the importance of the level (how many times has the price reached the level).
     */
    public record Level(TimeFrame.Scope timeFrame, long averageVolume, int importance) {

    }
}