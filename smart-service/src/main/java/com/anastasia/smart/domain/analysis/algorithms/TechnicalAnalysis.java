package com.anastasia.smart.domain.analysis.algorithms;

import com.anastasia.smart.entities.candles.PriceCandle;
import com.anastasia.smart.entities.candles.PricePoint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class TechnicalAnalysis {

    private TechnicalAnalysis(){}


    public static List<PricePoint> findLocalHighs(List<PriceCandle> candles) {
        List<PricePoint> localHighs = new ArrayList<>();

        for (int i = 1; i < candles.size() - 1; i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            PriceCandle next = candles.get(i + 1);

            if (curr.high().compareTo(prev.high()) > 0 && curr.high().compareTo(next.high()) > 0) {
                localHighs.add(new PricePoint(i, curr.high()));
            }
        }
        return localHighs;
    }

    public static List<PriceCandle> findLocalHighsCandles(List<PriceCandle> candles) {
        List<PriceCandle> localHighs = new ArrayList<>();

        for (int i = 1; i < candles.size() - 1; i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            PriceCandle next = candles.get(i + 1);

            if (curr.high().compareTo(prev.high()) > 0 && curr.high().compareTo(next.high()) > 0) {
                localHighs.add(curr);
            }
        }
        return localHighs;
    }

    public static List<PricePoint> findLocalLows(List<PriceCandle> candles) {
        List<PricePoint> localLows = new ArrayList<>();

        for (int i = 1; i < candles.size() - 1; i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            PriceCandle next = candles.get(i + 1);

            if (curr.low().compareTo(prev.low()) < 0 && curr.low().compareTo(next.low()) < 0) {
                localLows.add(new PricePoint(i, curr.low()));
            }
        }
        return localLows;
    }

    public static List<PriceCandle> findLocalLowsCandles(List<PriceCandle> candles) {
        List<PriceCandle> localLows = new ArrayList<>();

        for (int i = 1; i < candles.size() - 1; i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            PriceCandle next = candles.get(i + 1);

            if (curr.low().compareTo(prev.low()) < 0 && curr.low().compareTo(next.low()) < 0) {
                localLows.add(curr);
            }
        }
        return localLows;
    }

    public static double ATR(List<PriceCandle> candles, double baseSensitivity) {
        double averageTrueRange = 0.0;
        for (int i = 1; i < candles.size(); i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            double trueRange = Math.max(curr.high().doubleValue(), prev.close().doubleValue()) -
                    Math.min(curr.low().doubleValue(), prev.close().doubleValue());
            averageTrueRange += trueRange;
        }
        averageTrueRange /= candles.size();
        return baseSensitivity * (averageTrueRange / candles.getFirst().close().doubleValue());
    }

}
