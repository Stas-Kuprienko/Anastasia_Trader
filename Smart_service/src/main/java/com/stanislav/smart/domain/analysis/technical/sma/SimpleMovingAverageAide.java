/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.domain.analysis.technical.sma;

import com.stanislav.smart.domain.analysis.AnalysisAide;
import com.stanislav.smart.entities.TimeFrame;
import com.stanislav.smart.entities.candles.PriceCandle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleMovingAverageAide implements AnalysisAide {

    private final TimeFrame.Scope timeFrame;
    private final int period;
    private final ArrayList<PriceCandle> candlesList;
    private final ArrayList<SMAValue> smaValues;


    SimpleMovingAverageAide(TimeFrame.Scope timeFrame, List<? extends PriceCandle> candles, int period) {
        this.timeFrame = timeFrame;
        this.candlesList = new ArrayList<>(candles);
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
            sum += candlesList.get(i).close().doubleValue();
        }

        // to avoid counting all sum every time, keep var of sum
        //just subtract first period value and add next value every iteration
        for (; i < candlesList.size(); i++) {
            double newValue = sum / period;
            smaValues.add(new SMAValue(candlesList.get(i).dateTime(), newValue));
            sum -= candlesList.get(i - period).close().doubleValue();
            sum += candlesList.get(i).close().doubleValue();
        }
    }

    public double update(double price) {
        if (smaValues.isEmpty()) {
            throw new IllegalStateException("SMA value list is empty");
        }
        // take the last average value from the list and multiply by the period
        //to get the sum of the last period of values
        double lastPeriodSumValue = (smaValues.getLast().value) * period;

        // take the first intraDayCandle of the last period of values to subtract from the last sum
        //so as not to count everything over again
        double firstCandleOfLastPeriodSum = candlesList.get((candlesList.size() - 1) - period).close().doubleValue();
        double newValue = lastPeriodSumValue - firstCandleOfLastPeriodSum;
        newValue += price;
        double result = newValue / period;
        String dateTime;
        if (timeFrame.getClass().equals(TimeFrame.Day.class)) {
            dateTime = LocalDate.now().toString();
        } else if (timeFrame.getClass().equals(TimeFrame.IntraDay.class)) {
            dateTime = LocalDateTime.now().toString();
        } else {
            throw new IllegalStateException("timeframe class is incorrect - " + timeFrame.getClass());
        }
        smaValues.add(new SMAValue(dateTime, result));
        return result;
    }

    public double last() {
        if (!smaValues.isEmpty()) {
            return smaValues.getLast().value;
        }
        return 0;
    }

    public TimeFrame.Scope getTimeFrame() {
        return timeFrame;
    }

    public int getPeriod() {
        return period;
    }

    public ArrayList<PriceCandle> getCandles() {
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