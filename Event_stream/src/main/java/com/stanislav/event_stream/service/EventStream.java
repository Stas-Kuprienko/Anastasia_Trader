package com.stanislav.event_stream.service;

public interface EventStream {

    EventStreamListener subscribe(String board, String ticker);

    void unsubscribe(EventStreamListener listener);

    void unsubscribe(EventStreamListener listener, boolean isForced);

    EventStreamListener getEventStream(String ticker);
}
