package com.stanislav.smart_analytics.domain.market.finam.candles;

import com.google.protobuf.Timestamp;
import com.stanislav.smart_analytics.domain.entities.Decimal;
import com.stanislav.smart_analytics.domain.entities.candles.IntraDayCandles;
import proto.tradeapi.v1.Candles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public record FinamIntraDayCandlesProxy(FinamIntraDayCandleProxy[] candles) implements IntraDayCandles {

    @Override
    public String toString() {
        return "IntraDayCandles{" +
                "candles=" + Arrays.toString(candles) +
                '}';
    }

    public record FinamIntraDayCandleProxy(Candles.IntradayCandle intradayCandle) implements Candle {

        @Override
        public String dateTime() {
            Timestamp timestamp = intradayCandle.getTimestamp();
            return LocalDateTime.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos(), ZoneOffset.UTC).toString();
        }

        @Override
        public Decimal open() {
            return new Decimal(intradayCandle.getOpen().getNum(), intradayCandle.getOpen().getScale());
        }

        @Override
        public Decimal close() {
            return new Decimal(intradayCandle.getClose().getNum(), intradayCandle.getClose().getScale());
        }

        @Override
        public Decimal high() {
            return new Decimal(intradayCandle.getHigh().getNum(), intradayCandle.getHigh().getScale());
        }

        @Override
        public Decimal low() {
            return new Decimal(intradayCandle.getLow().getNum(), intradayCandle.getLow().getScale());
        }

        @Override
        public long volume() {
            return intradayCandle.getVolume();
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
