package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.Candles;

import java.math.BigDecimal;
import java.util.HashMap;

public class SupportResistanceLevelsAide implements AnalysisAide {

    private final String ticker;
    private final Candles candles;
    private final HashMap<PriceRange, Level> levels;


    public SupportResistanceLevelsAide(String ticker, Candles candles) {
        this.ticker = ticker;
        this.candles = candles;
        this.levels = new HashMap<>();
        calculate();
    }

    public void calculate() {

    }

    public void addLevel(BigDecimal from, BigDecimal to) {

    }

    public String getTicker() {
        return ticker;
    }

    public Candles getCandles() {
        return candles;
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
     * @param spread The average value of the price range. These are percentage units.
     * @param importance The index of the importance of the level (how many times has the price reached the level).
     */
    public record Level(TimeFrame.Scope timeFrame, long averageVolume, int importance) {

    }
}