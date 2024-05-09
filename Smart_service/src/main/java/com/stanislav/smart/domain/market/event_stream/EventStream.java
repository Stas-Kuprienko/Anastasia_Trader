package com.stanislav.smart.domain.market.event_stream;

import com.stanislav.smart.domain.entities.Security;

public interface EventStream {

    EventStreamListener subscribe(Security security);

    void unsubscribe(EventStreamListener listener);

    void unsubscribe(EventStreamListener listener, boolean isForced);

    EventStreamListener getEventStream(Security security);
}
