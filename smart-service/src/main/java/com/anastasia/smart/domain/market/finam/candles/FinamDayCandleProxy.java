package com.anastasia.smart.domain.market.finam.candles;

import com.google.type.Date;
import com.anastasia.smart.entities.candles.DayCandle;
import proto.tradeapi.v1.Candles;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FinamDayCandleProxy(
        Candles.DayCandle dayCandle) implements DayCandle {

    @Override
    public String dateTime() {
        Date date = dayCandle.getDate();
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay()).toString();
    }

    @Override
    public BigDecimal open() {
        return BigDecimal.valueOf(dayCandle.getOpen().getNum(), dayCandle.getOpen().getScale());
    }

    @Override
    public BigDecimal close() {
        return BigDecimal.valueOf(dayCandle.getClose().getNum(), dayCandle.getClose().getScale());
    }

    @Override
    public BigDecimal high() {
        return BigDecimal.valueOf(dayCandle.getHigh().getNum(), dayCandle.getHigh().getScale());
    }

    @Override
    public BigDecimal low() {
        return BigDecimal.valueOf(dayCandle.getLow().getNum(), dayCandle.getLow().getScale());
    }

    @Override
    public long volume() {
        return dayCandle.getVolume();
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
