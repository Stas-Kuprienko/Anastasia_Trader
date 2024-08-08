package com.anastasia.trade.domain.trading.finam;

import com.anastasia.trade.domain.trading.OrderCriteria;
import com.anastasia.trade.domain.trading.finam.order_dto.BuySell;
import com.anastasia.trade.domain.trading.finam.order_dto.OrderCondition;
import com.anastasia.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.anastasia.trade.domain.trading.finam.order_dto.OrderValidBefore;
import com.anastasia.trade.entities.Direction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderRequestAdaptor {


    public FinamOrderRequest parse(OrderCriteria criteria) {
        FinamOrderRequest.Property prop = switch (criteria.getFulFillProperty()) {
            case PutInQueue -> FinamOrderRequest.Property.PutInQueue;
            case CancelUnfulfilled -> FinamOrderRequest.Property.CancelBalance;
        };
        BuySell buySell = switch (criteria.getDirection()) {
            case Buy -> BuySell.Buy;
            case Sell -> BuySell.Sell;
            case null, default ->
                    throw new IllegalArgumentException("direction=" + criteria.getDirection() + " in " + criteria);
        };
        OrderCondition orderCond = parserOrderCondition(criteria);
        OrderValidBefore orderValid = parseOrderValidBefore(criteria);
        return new FinamOrderRequest(
                criteria.getClientId(),
                criteria.getBoard().name(),
                criteria.getTicker(),
                buySell,
                criteria.getQuantity(),
                criteria.isMargin(),
                criteria.getPrice(),
                prop,
                orderCond,
                orderValid
        );
    }


    private OrderValidBefore parseOrderValidBefore(OrderCriteria criteria) {
        OrderValidBefore.Type validBeforeType;
        String time;
        switch (criteria.getValidBefore()) {
            case TillCancelled -> {
                validBeforeType = OrderValidBefore.Type.TillCancelled;
                time = null;
            }
            case TillEndSession -> {
                validBeforeType = OrderValidBefore.Type.TillEndSession;
                time = null;
            }
            case ExactTime -> {
                if (criteria.getBeforeTime() == null) {
                    throw new IllegalArgumentException("beforeTime is null in " + criteria);
                }
                validBeforeType = OrderValidBefore.Type.ExactTime;
                time = criteria.getBeforeTime().toString();
            }
            case null, default ->
                    throw new IllegalArgumentException("validBefore=" + criteria.getValidBefore() + " in " + criteria);
        }
        return new OrderValidBefore(validBeforeType, time);
    }

    private OrderCondition parserOrderCondition(OrderCriteria criteria) {
        OrderCondition.Type condType;
        BigDecimal price;
        String time;
        switch (criteria.getPriceType()) {
            case MarketPrice -> {
                condType = marketPriceCondition(criteria.getDirection());
                price = BigDecimal.valueOf(0);
                time = null;
            }
            case Limit -> {
                condType = limitCondition(criteria.getDirection());
                price = BigDecimal.valueOf(criteria.getPrice());
                time = null;
            }
            case Delayed -> {
                if (criteria.getDelayTime() == null) {
                    throw new IllegalArgumentException("delayTime is null in " + criteria);
                }
                if (criteria.getPrice() > 0) {
                    condType = limitCondition(criteria.getDirection());
                } else {
                    condType = marketPriceCondition(criteria.getDirection());
                }
                price = BigDecimal.valueOf(criteria.getPrice());
                time = criteria.getDelayTime().toString();
            }
            case null, default -> throw new IllegalArgumentException("priceType=" + criteria.getPriceType() + " in " + criteria);
        }
        return new OrderCondition(condType, price, time);
    }

    private OrderCondition.Type marketPriceCondition(Direction direction) {
        OrderCondition.Type condType = direction.equals(Direction.Buy) ?
                OrderCondition.Type.Ask : direction.equals(Direction.Sell) ?
                OrderCondition.Type.Bid : null;
        if (condType == null) {
            throw new IllegalArgumentException("direction=" + direction.name());
        }
        return condType;
    }

    private OrderCondition.Type limitCondition(Direction direction) {
        OrderCondition.Type condType = direction.equals(Direction.Buy) ?
                OrderCondition.Type.LastDown : direction.equals(Direction.Sell) ?
                OrderCondition.Type.LastUp : null;
        if (condType == null) {
            throw new IllegalArgumentException("direction=" + direction.name());
        }
        return condType;
    }
}
