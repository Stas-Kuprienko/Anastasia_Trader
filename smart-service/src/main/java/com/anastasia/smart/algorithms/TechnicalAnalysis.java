package com.anastasia.smart.algorithms;

import com.anastasia.smart.algorithms.model.*;
import com.anastasia.smart.entities.candles.PriceCandle;
import com.anastasia.smart.algorithms.model.PricePoint;
import java.util.ArrayList;
import java.util.List;

/**
 * The utility class of technical analysis algorithms. Usable to analyze {@link PriceCandle candles} of stock exchange charts.
 */
public final class TechnicalAnalysis {

    private TechnicalAnalysis(){}


    public static List<PricePoint> findLocalHighs(List<PriceCandle> candles) {
        List<PricePoint> localHighs = new ArrayList<>();

        for (int i = 1; i < candles.size() - 1; i++) {
            PriceCandle prev = candles.get(i - 1);
            PriceCandle curr = candles.get(i);
            PriceCandle next = candles.get(i + 1);

            if (curr.high().compareTo(prev.high()) > 0 && curr.high().compareTo(next.high()) > 0) {
                localHighs.add(new PricePoint(curr.dateTime(), curr.high()));
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
                localLows.add(new PricePoint(curr.dateTime(), curr.low()));
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


    /**
     * Method to dynamically adjust sensitivity based on volatility. Calculate the average true range (ATR).
     * @param candles the List of the {@link PriceCandle candles}.
     * @param baseSensitivity the base sensitivity value for calculating.
     * @return the average true range (ATR) double value.
     */
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


    public static List<SupportLevel> supportLevels(List<PriceCandle> candles, double sensitivity, int minTouches, boolean withATR) {
        if (withATR) {
            sensitivity = ATR(candles, sensitivity);
        }
        List<PricePoint> localLows = findLocalLows(candles);
        List<SupportLevel> supportLevels = new ArrayList<>();

        for (PricePoint low : localLows) {
            boolean foundCluster = false;
            double price = low.price().doubleValue();

            for (SupportLevel support : supportLevels) {
                if (Math.abs(support.getLevel() - price) <= sensitivity) {
                    support.addTouch(low);
                    foundCluster = true;
                    break;
                }
            }
            if (!foundCluster) {
                SupportLevel newSupport = new SupportLevel(price);
                newSupport.addTouch(low);
                supportLevels.add(newSupport);
            }
        }
        supportLevels.removeIf(support -> support.getTouchCount() < minTouches);
        return supportLevels;
    }


    /**
     * Method detect and merge close support levels into broader zones, considering a specific zoneWidth.
     * @param supportLevels the {@link List} of {@link SupportLevel support levels}.
     * @param zoneWidth Width of the support zone
     * @return the list of {@link SupportZone support zones}.
     */
    public static List<SupportZone> supportZones(List<SupportLevel> supportLevels, double zoneWidth) {
        List<SupportZone> supportZones = new ArrayList<>();

        for (SupportLevel level : supportLevels) {
            boolean merged = false;

            for (SupportZone zone : supportZones) {
                if (Math.abs(zone.getCenter() - level.getLevel()) <= zoneWidth) {
                    zone.addLevel(level.getLevel());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                supportZones.add(new SupportZone(level.getLevel(), zoneWidth));
            }
        }
        return supportZones;
    }


    public static List<ResistanceLevel> resistanceLevels(List<PriceCandle> candles, double sensitivity, int minTouches, boolean withATR) {
        if (withATR) {
            sensitivity = ATR(candles, sensitivity);
        }
        List<PricePoint> localHighs = findLocalHighs(candles);
        List<ResistanceLevel> resistanceLevels = new ArrayList<>();

        for (PricePoint high : localHighs) {
            boolean foundCluster = false;
            double price = high.price().doubleValue();

            for (ResistanceLevel resistance : resistanceLevels) {
                if (Math.abs(resistance.getLevel() - price) <= sensitivity) {
                    resistance.addTouch(high);
                    foundCluster = true;
                    break;
                }
            }
            if (!foundCluster) {
                ResistanceLevel newResistance = new ResistanceLevel(price);
                newResistance.addTouch(high);
                resistanceLevels.add(newResistance);
            }
        }
        resistanceLevels.removeIf(resistance -> resistance.getTouchCount() < minTouches);
        return resistanceLevels;
    }


    /**
     * Method detect and merge close resistance levels into broader zones, considering a specific zoneWidth.
     * @param resistanceLevels the {@link List} of {@link ResistanceLevel resistance levels}.
     * @param zoneWidth Width of the resistance zone
     * @return the list of {@link ResistanceZone resistance zones}.
     */
    public List<ResistanceZone> resistanceZones(List<ResistanceLevel> resistanceLevels, double zoneWidth) {
        List<ResistanceZone> resistanceZones = new ArrayList<>();

        for (ResistanceLevel level : resistanceLevels) {
            boolean merged = false;

            for (ResistanceZone zone : resistanceZones) {
                if (Math.abs(zone.getCenter() - level.getLevel()) <= zoneWidth) {
                    zone.addLevel(level.getLevel());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                resistanceZones.add(new ResistanceZone(level.getLevel(), zoneWidth));
            }
        }
        return resistanceZones;
    }


    /**
     * Method identifies areas where support and resistance levels overlap, forming confluence levels.
     * @param supportLevels the {@link List} of {@link SupportLevel support levels}.
     * @param resistanceLevels the {@link List} of {@link ResistanceLevel resistance levels}.
     * @param confluenceTolerance The confluenceTolerance parameter determines
     *                            how close the support and resistance levels must be to form a confluence.
     * @return the {@link List} of {@link SupportResistanceLevel confluence levels}.
     */
    public static List<SupportResistanceLevel> confluenceLevels(List<SupportLevel> supportLevels,
                                                                      List<ResistanceLevel> resistanceLevels,
                                                                      double confluenceTolerance) {
        List<SupportResistanceLevel> confluenceLevels = new ArrayList<>();

        for (SupportLevel support : supportLevels) {
            for (ResistanceLevel resistance : resistanceLevels) {
                if (Math.abs(support.getLevel() - resistance.getLevel()) <= confluenceTolerance) {
                    confluenceLevels.add(new SupportResistanceLevel(support.getLevel(), support.getTouchCount(), resistance.getTouchCount()));
                }
            }
        }
        return confluenceLevels;
    }


    /**
     * Method identifies areas where support and resistance zones overlap, forming confluence zones.
     * @param supportZones the {@link List} of {@link SupportZone support zones}.
     * @param resistanceZones the {@link List} of {@link ResistanceZone resistance zones}.
     * @param confluenceTolerance The confluenceTolerance parameter determines
     *                            how close the support and resistance zones must be to form a confluence.
     * @return the {@link List} of {@link SupportResistanceZone confluence zones}.
     */
    public static List<SupportResistanceZone> confluenceZones(List<SupportZone> supportZones,
                                                                    List<ResistanceZone> resistanceZones,
                                                                    double confluenceTolerance) {
        List<SupportResistanceZone> confluenceZones = new ArrayList<>();

        for (SupportZone support : supportZones) {
            for (ResistanceZone resistance : resistanceZones) {
                if (support.getMaxLevel() >= resistance.getMinLevel() - confluenceTolerance &&
                        support.getMinLevel() <= resistance.getMaxLevel() + confluenceTolerance) {
                    double minConfluenceLevel = Math.max(support.getMinLevel(), resistance.getMinLevel());
                    double maxConfluenceLevel = Math.min(support.getMaxLevel(), resistance.getMaxLevel());
                    confluenceZones.add(new SupportResistanceZone(minConfluenceLevel, maxConfluenceLevel));
                }
            }
        }
        return confluenceZones;
    }
}
