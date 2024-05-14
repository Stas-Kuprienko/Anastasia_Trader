package com.stanislav.domain.trading.finam;

import com.stanislav.domain.trading.TradeCriteria;
import com.stanislav.domain.trading.finam.order_dto.FinamOrderValidBefore;
import com.stanislav.domain.trading.finam.order_dto.FinamOrderCondition;
import com.stanislav.domain.trading.finam.order_dto.FinamOrderRequest;
import com.stanislav.entities.Direction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinamOrderTradeCriteria(FinamOrderValidBefore validBefore,
                                      FinamOrderCondition condition,
                                      FinamOrderRequest.Property property,
                                      boolean useCredit) implements TradeCriteria {

    public static FinamOrderTradeCriteria simpleOrderAtMarketPrice(Direction direction) {
        String time = LocalDateTime.now().toString();
        FinamOrderValidBefore.Type validType = FinamOrderValidBefore.Type.TillEndSession;
        FinamOrderValidBefore validBefore = new FinamOrderValidBefore(validType, time);
        FinamOrderCondition.Type conditionType = direction.equals(Direction.Buy) ?
                FinamOrderCondition.Type.Ask : FinamOrderCondition.Type.Bid;
        FinamOrderCondition condition = new FinamOrderCondition(conditionType, BigDecimal.valueOf(0), time);
        FinamOrderRequest.Property property = FinamOrderRequest.Property.CancelBalance;
        return new FinamOrderTradeCriteria(validBefore, condition, property, false);
    }


}
