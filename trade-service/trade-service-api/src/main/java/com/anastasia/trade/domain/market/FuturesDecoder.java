package com.anastasia.trade.domain.market;

import java.time.LocalDate;

public interface FuturesDecoder {
    LocalDate decodeExpiration(String ticker);
}
