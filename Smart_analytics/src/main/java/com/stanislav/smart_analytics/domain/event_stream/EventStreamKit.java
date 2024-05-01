package com.stanislav.smart_analytics.domain.event_stream;

import com.stanislav.smart_analytics.domain.event_stream.service.EventStream;

public interface EventStreamKit {
    EventStream getOrderBookStreamService();
}
