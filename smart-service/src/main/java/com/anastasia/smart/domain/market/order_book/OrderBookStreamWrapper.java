package com.anastasia.smart.domain.market.order_book;

import proto.tradeapi.v1.Events;

public record OrderBookStreamWrapper(OrderBookStream<Events.OrderBookRow> orderBookStream,
                                     Events.SubscriptionRequest subscription,
                                     Events.SubscriptionRequest unsubscription) {}