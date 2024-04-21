package com.stanislav.event_stream;

import com.stanislav.event_stream.service.EventStream;

public interface EventStreamKit {
    EventStream getOrderBookStreamService();
}
