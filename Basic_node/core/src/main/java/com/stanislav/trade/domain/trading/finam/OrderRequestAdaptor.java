package com.stanislav.trade.domain.trading.finam;

import com.stanislav.trade.domain.trading.OrderCriteria;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamBuySell;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderCondition;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderValidBefore;
import com.stanislav.trade.entities.Direction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderRequestAdaptor {


    public FinamOrderRequest parse(OrderCriteria criteria) {
        FinamOrderRequest.Property prop = switch (criteria.getFulFillProperty()) {
            case PutInQueue -> FinamOrderRequest.Property.PutInQueue;
            case CancelUnfulfilled -> FinamOrderRequest.Property.CancelBalance;
        };
        FinamBuySell buySell = switch (criteria.getDirection()) {
            case Buy -> FinamBuySell.Buy;
            case Sell -> FinamBuySell.Sell;
            case null, default ->
                    throw new IllegalArgumentException("direction=" + criteria.getDirection() + " in " + criteria);
        };
        FinamOrderCondition orderCond = parserOrderCondition(criteria);
        FinamOrderValidBefore orderValid = parseOrderValidBefore(criteria);
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


    private FinamOrderValidBefore parseOrderValidBefore(OrderCriteria criteria) {
        FinamOrderValidBefore.Type validBeforeType;
        String time;
        switch (criteria.getValidBefore()) {
            case TillCancelled -> {
                validBeforeType = FinamOrderValidBefore.Type.TillCancelled;
                time = null;
            }
            case TillEndSession -> {
                validBeforeType = FinamOrderValidBefore.Type.TillEndSession;
                time = null;
            }
            case ExactTime -> {
                if (criteria.getBeforeTime() == null) {
                    throw new IllegalArgumentException("beforeTime is null in " + criteria);
                }
                validBeforeType = FinamOrderValidBefore.Type.ExactTime;
                time = criteria.getBeforeTime().toString();
            }
            case null, default ->
                    throw new IllegalArgumentException("validBefore=" + criteria.getValidBefore() + " in " + criteria);
        }
        return new FinamOrderValidBefore(validBeforeType, time);
    }

    private FinamOrderCondition parserOrderCondition(OrderCriteria criteria) {
        FinamOrderCondition.Type condType;
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
        return new FinamOrderCondition(condType, price, time);
    }

    private FinamOrderCondition.Type marketPriceCondition(Direction direction) {
        FinamOrderCondition.Type condType = direction.equals(Direction.Buy) ?
                FinamOrderCondition.Type.Ask : direction.equals(Direction.Sell) ?
                FinamOrderCondition.Type.Bid : null;
        if (condType == null) {
            throw new IllegalArgumentException("direction=" + direction.name());
        }
        return condType;
    }

    private FinamOrderCondition.Type limitCondition(Direction direction) {
        FinamOrderCondition.Type condType = direction.equals(Direction.Buy) ?
                FinamOrderCondition.Type.LastDown : direction.equals(Direction.Sell) ?
                FinamOrderCondition.Type.LastUp : null;
        if (condType == null) {
            throw new IllegalArgumentException("direction=" + direction.name());
        }
        return condType;
    }
}
