package com.anastasia.trade.domain.market.moex;

import com.anastasia.trade.domain.market.FuturesDecoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("futuresDecoder")
public class MoexFuturesDecoder implements FuturesDecoder {

    private static final byte EXPIRATION_DAY_OF_MONTH = 15;

    @Override
    public LocalDate decodeExpiration(String ticker) {
        int tickerLength = ticker.length();
        if (tickerLength < 3) {
            throw new IllegalArgumentException(ticker);
        }
        try {
            int futuresYear = Integer.parseInt(ticker.substring(tickerLength - 1));
            int year = LocalDate.now().getYear();
            for (; ; ) {
                if ((year % 10) < futuresYear) {
                    year += 1;
                } else if ((year % 10) > futuresYear) {
                    year -= 1;
                } else {
                    break;
                }
            }
            String futuresMonth = String.valueOf(ticker.charAt(tickerLength - 2));
            int monthValue = ExpirationMonths.valueOf(futuresMonth.toUpperCase()).ordinal() + 1;
            return LocalDate.of(year, monthValue, EXPIRATION_DAY_OF_MONTH);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ticker);
        }
    }

    enum ExpirationMonths {
        F, G, H, J, K, M, N, Q, U, V, X, Z
    }
}