package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.candles.Candles;

import java.math.BigDecimal;
import java.util.HashMap;

public class SupportResistanceLinesAide implements AnalysisAide {

    private final Candles candles;
    private final HashMap<BigDecimal, Line> lines;


    public SupportResistanceLinesAide(Candles candles) {
        this.candles = candles;
        this.lines = new HashMap<>();
        calculate();
    }

    public void calculate() {

    }

    public Candles getCandles() {
        return candles;
    }

    public HashMap<BigDecimal, Line> getLines() {
        return lines;
    }


    /**
     * The description of support/resistance line parameters.
     * @param averageVolume The average value of trading volumes around the price range of the line.
     * @param spread The average value of the price range. These are percentage units.
     * @param importance The index of the importance of the line,
     *                   how prevalent it is and how strong this line is.
     */
    public record Line (long averageVolume, byte spread, int importance) {

    }
}