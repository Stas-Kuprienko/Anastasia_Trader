package com.anastasia.smart.domain.market.order_book.finam;

import com.anastasia.smart.domain.market.order_book.OrderBookStream;
import proto.tradeapi.v1.Events;

public record OrderBookStreamWrapper(OrderBookStream<Events.OrderBookRow> orderBookStream,
                                     Events.SubscriptionRequest subscription,
                                     Events.SubscriptionRequest unsubscription) {}