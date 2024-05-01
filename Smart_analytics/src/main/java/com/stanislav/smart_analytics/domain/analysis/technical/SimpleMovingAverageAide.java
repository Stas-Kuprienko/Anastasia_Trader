package com.stanislav.smart_analytics.domain.analysis.technical;

import com.stanislav.smart_analytics.domain.entities.candles.Candles;

import java.util.ArrayList;
import java.util.List;

public class SimpleMovingAverageAide {

    private final ArrayList<Candles.Candle> candlesList;
    private final int period;
    private final ArrayList<SMAValue> smaValues;


    public SimpleMovingAverageAide(Candles candles, int period) {
        this.candlesList = new ArrayList<>(List.of(candles.candles()));
        this.period = period;
        smaValues = new ArrayList<>();
        calculate();
    }


    public void calculate() {
        if (period < 2 || candlesList.size() <= period) {
            throw new IllegalArgumentException("period value is incorrect - " + period);
        }
        double sum = 0;
        int i = 0;

        //count first period sum of values
        for (; i < period; i++) {
            sum += candlesList.get(i).close().toDouble();
        }

        //to avoid counting all sum every time, keep var of sum
        // just subtract first period value and add next value every iteration
        for (; i < candlesList.size(); i++) {
            double newValue = sum / period;
            smaValues.add(new SMAValue(candlesList.get(i).dateTime(), newValue));
            sum -= candlesList.get(i - period).close().toDouble();
            sum += candlesList.get(i).close().toDouble();
        }
    }

    public double update(Candles.Candle candle) {
        if (smaValues.isEmpty()) {
            throw new IllegalStateException("SMA value list is empty");
        }
        double num = candle.close().toDouble();

        //take the last average value from the list and multiply by the period
        // to get the sum of the last period of values
        double lastAverageValue = (smaValues.get(smaValues.size() - 1).value) * period;

        //take the first candle of the last period of values to subtract from the last sum
        // so as not to count everything over again
        double firstCandleOfLastPeriodSum = candlesList.get((candlesList.size() - 1) - period).close().toDouble();
        double newValue = lastAverageValue - firstCandleOfLastPeriodSum;
        newValue += num;
        double result = newValue / period;
        smaValues.add(new SMAValue(candle.dateTime(), result));
        candlesList.add(candle);
        return result;
    }

    public ArrayList<Candles.Candle> getCandles() {
        return candlesList;
    }

    public ArrayList<SMAValue> getSmaValues() {
        return smaValues;
    }

    public record SMAValue(String date, double value) {}
}