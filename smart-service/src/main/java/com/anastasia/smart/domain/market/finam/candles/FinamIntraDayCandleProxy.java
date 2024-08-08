package com.anastasia.smart.domain.market.finam.candles;

import com.google.protobuf.Timestamp;
import com.anastasia.smart.entities.candles.IntraDayCandle;
import proto.tradeapi.v1.Candles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record FinamIntraDayCandleProxy(
        Candles.IntradayCandle intradayCandle) implements IntraDayCandle {

    @Override
    public String dateTime() {
        Timestamp timestamp = intradayCandle.getTimestamp();
        return LocalDateTime.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos(), ZoneOffset.UTC).toString();
    }

    @Override
    public BigDecimal open() {
        return BigDecimal.valueOf(intradayCandle.getOpen().getNum(), intradayCandle.getOpen().getScale());
    }

    @Override
    public BigDecimal close() {
        return BigDecimal.valueOf(intradayCandle.getClose().getNum(), intradayCandle.getClose().getScale());
    }

    @Override
    public BigDecimal high() {
        return BigDecimal.valueOf(intradayCandle.getHigh().getNum(), intradayCandle.getHigh().getScale());
    }

    @Override
    public BigDecimal low() {
        return BigDecimal.valueOf(intradayCandle.getLow().getNum(), intradayCandle.getLow().getScale());
    }

    @Override
    public long volume() {
        return intradayCandle.getVolume();
    }

    @Override
    public String toString() {
        return "PriceCandle{" +
                "date='" + dateTime() + '\'' +
                ", open=" + open() +
                ", close=" + close() +
                ", high=" + high() +
                ", low=" + low() +
                ", volume=" + volume() +
                '}';
    }
}
