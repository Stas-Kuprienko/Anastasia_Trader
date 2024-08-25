package com.anastasia.smart.algorithms.model;

import java.math.BigDecimal;

public record PriceCandle(String dateTime,
                          BigDecimal open,
                          BigDecimal high,
                          BigDecimal low,
                          BigDecimal close,
                          int volume) {}