package com.stanislav.smart.domain.market.event_stream;

import stanislav.anastasia.trade.Smart;

public interface EventStream {

    EventStreamListener subscribe(Smart.Security security);

    void unsubscribe(EventStreamListener listener);

    void unsubscribe(EventStreamListener listener, boolean isForced);

    EventStreamListener getEventStreamListener(Smart.Security security);
}
