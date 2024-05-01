package com.stanislav.smart_analytics.domain.event_stream;

public interface EventStream {

    EventStreamListener subscribe(String board, String ticker);

    void unsubscribe(EventStreamListener listener);

    void unsubscribe(EventStreamListener listener, boolean isForced);

    EventStreamListener getEventStream(String ticker);
}
