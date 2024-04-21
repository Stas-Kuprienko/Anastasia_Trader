package com.stanislav.event_stream;

public interface EventStreamService {

    void subscribe(String board, String ticker) throws InterruptedException;

    void unsubscribe(String board, String ticker);

    EventStreamListener getEventStream(String ticker);
}
