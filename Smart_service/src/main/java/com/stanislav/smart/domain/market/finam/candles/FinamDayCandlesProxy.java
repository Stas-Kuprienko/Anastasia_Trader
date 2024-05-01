package com.stanislav.smart.domain.market.finam.candles;

import com.google.type.Date;
import com.stanislav.smart.domain.entities.candles.DayCandles;
import com.stanislav.smart.domain.entities.Decimal;
import proto.tradeapi.v1.Candles;

import java.time.LocalDate;
import java.util.Arrays;

public record FinamDayCandlesProxy(FinamDayCandleProxy[] candles) implements DayCandles {

    @Override
    public String toString() {
        return "DayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record FinamDayCandleProxy(Candles.DayCandle dayCandle) implements Candle {

        @Override
        public String dateTime() {
            Date date = dayCandle.getDate();
            return LocalDate.of(date.getYear(), date.getMonth(), date.getDay()).toString();
        }

        @Override
        public Decimal open() {
            return new Decimal(dayCandle.getOpen().getNum(), dayCandle.getOpen().getScale());
        }

        @Override
        public Decimal close() {
            return new Decimal(dayCandle.getClose().getNum(), dayCandle.getClose().getScale());
        }

        @Override
        public Decimal high() {
            return new Decimal(dayCandle.getHigh().getNum(), dayCandle.getHigh().getScale());
        }

        @Override
        public Decimal low() {
            return new Decimal(dayCandle.getLow().getNum(), dayCandle.getLow().getScale());
        }

        @Override
        public long volume() {
            return dayCandle.getVolume();
        }

        @Override
        public String toString() {
            return "Candle{" +
                    "date='" + dateTime() + '\'' +
                    ", open=" + open() +
                    ", close=" + close() +
                    ", high=" + high() +
                    ", low=" + low() +
                    ", volume=" + volume() +
                    '}';
        }
    }
}
