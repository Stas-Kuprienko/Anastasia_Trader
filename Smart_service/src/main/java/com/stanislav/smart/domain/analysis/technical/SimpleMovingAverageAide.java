package com.stanislav.smart.domain.analysis.technical;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.Candles;

import java.util.ArrayList;
import java.util.List;

public class SimpleMovingAverageAide implements AnalysisAide {

    private final TimeFrame.Scope timeFrame;
    private final int period;
    private final ArrayList<Candles.Candle> candlesList;
    private final ArrayList<SMAValue> smaValues;


    public SimpleMovingAverageAide(TimeFrame.Scope timeFrame, Candles candles, int period) {
        this.timeFrame = timeFrame;
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

        // count first period sum of values
        for (; i < period; i++) {
            sum += candlesList.get(i).close().toDouble();
        }

        // to avoid counting all sum every time, keep var of sum
        //just subtract first period value and add next value every iteration
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
        double price = candle.close().toDouble();

        // take the last average value from the list and multiply by the period
        //to get the sum of the last period of values
        double lastPeriodSumValue = (smaValues.get(smaValues.size() - 1).value) * period;

        // take the first intraDayCandle of the last period of values to subtract from the last sum
        //so as not to count everything over again
        double firstCandleOfLastPeriodSum = candlesList.get((candlesList.size() - 1) - period).close().toDouble();
        double newValue = lastPeriodSumValue - firstCandleOfLastPeriodSum;
        newValue += price;
        double result = newValue / period;
        smaValues.add(new SMAValue(candle.dateTime(), result));
        candlesList.add(candle);
        return result;
    }

    public double last() {
        if (!smaValues.isEmpty()) {
            return smaValues.get(smaValues.size() - 1).value;
        }
        return 0;
    }

    public TimeFrame.Scope getTimeFrame() {
        return timeFrame;
    }

    public int getPeriod() {
        return period;
    }

    public ArrayList<Candles.Candle> getCandles() {
        return candlesList;
    }

    public ArrayList<SMAValue> getSmaValues() {
        return smaValues;
    }

    public record SMAValue(String date, double value) {}

    @Override
    public String toString() {
        return "SimpleMovingAverageAide{" +
                "period=" + period +
                ", smaValues=" + smaValues +
                '}';
    }
}