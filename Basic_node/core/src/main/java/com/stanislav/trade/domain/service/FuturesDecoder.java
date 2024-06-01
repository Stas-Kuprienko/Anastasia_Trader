package com.stanislav.trade.domain.service;

import java.time.LocalDate;

public interface FuturesDecoder {
    LocalDate decodeExpiration(String ticker);
}
