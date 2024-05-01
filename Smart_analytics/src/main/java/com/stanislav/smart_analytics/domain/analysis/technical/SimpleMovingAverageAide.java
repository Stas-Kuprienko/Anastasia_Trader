package com.stanislav.smart_analytics.domain.analysis.technical;

import com.stanislav.smart_analytics.domain.entities.candles.Candles;
import com.stanislav.smart_analytics.domain.entities.candles.Decimal;

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
            //TODO !!!!!!!!!!!!
            sum += candlesList.get(i).close().toDouble();
        }
        int scale = candlesList.get(i).close().scale();

        //to avoid counting all sum every time, keep var of sum
        // just subtract first period value and add next value every iteration
        for (; i < candlesList.size(); i++) {
            Decimal newValue = new Decimal(sum / period);
            smaValues.add(new SMAValue(candlesList.get(i).dateTime(), newValue));
            sum -= candlesList.get(i - period).close().num();
            sum += candlesList.get(i).close().num();
        }
    }

    public Decimal update(Candles.Candle candle) {
        if (smaValues.isEmpty()) {
            throw new IllegalStateException("SMA value list is empty");
        }
        long num = candle.close().num();

        //take the last average value from the list and multiply by the period
        // to get the sum of the last period of values
        long lastAverageValue = (smaValues.get(smaValues.size() - 1).value.num()) * period;

        //take the first candle of the last period of values to subtract from the last sum
        // so as not to count everything over again
        long firstCandleOfLastPeriodSum = candlesList.get((candlesList.size() - 1) - period).close().num();
        long newValue = lastAverageValue - firstCandleOfLastPeriodSum;
        newValue += num;
        Decimal result = new Decimal((int) (newValue / period), candle.close().scale());
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

    public record SMAValue(String date, Decimal value) {}
}