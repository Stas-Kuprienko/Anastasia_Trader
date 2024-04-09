package org.stanislav.event_stream;

public interface EventStreamService {

    void subscribe(String board, String ticker);

    void unsubscribe(String board, String ticker);
}
