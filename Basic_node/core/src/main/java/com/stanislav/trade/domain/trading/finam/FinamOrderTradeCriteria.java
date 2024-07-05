/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam;

import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.finam.order_dto.OrderValidBefore;
import com.stanislav.trade.domain.trading.finam.order_dto.OrderCondition;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.stanislav.trade.entities.Direction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinamOrderTradeCriteria(OrderValidBefore validBefore,
                                      OrderCondition condition,
                                      FinamOrderRequest.Property property,
                                      boolean useCredit) {

    public static FinamOrderTradeCriteria simpleOrderAtMarketPrice(Direction direction) {
        String time = LocalDateTime.now().toString();
        OrderValidBefore.Type validType = OrderValidBefore.Type.TillEndSession;
        OrderValidBefore validBefore = new OrderValidBefore(validType, time);
        OrderCondition.Type conditionType = direction.equals(Direction.Buy) ?
                OrderCondition.Type.Ask : OrderCondition.Type.Bid;
        OrderCondition condition = new OrderCondition(conditionType, BigDecimal.valueOf(0), time);
        FinamOrderRequest.Property property = FinamOrderRequest.Property.CancelBalance;
        return new FinamOrderTradeCriteria(validBefore, condition, property, false);
    }

    public static FinamOrderTradeCriteria parse(TradeCriteria tradeCriteria) {

        return null;
    }
}