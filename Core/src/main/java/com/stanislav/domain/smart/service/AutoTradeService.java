package com.stanislav.domain.smart.service;

import com.stanislav.entities.user.Account;
import com.stanislav.event_stream.EventStreamKit;
import com.stanislav.event_stream.service.EventStreamListener;
import com.stanislav.event_stream.service.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoTradeService {

    private final EventStream orderBookStreamService;

    public AutoTradeService(@Autowired EventStreamKit eventStreamKit) {
        this.orderBookStreamService = eventStreamKit.getOrderBookStreamService();
    }

    public void getPriceStream(Account account, String ticker, String board) {
        EventStreamListener listener = orderBookStreamService.getEventStream(ticker);
        if (!hasRelevantListener(listener)) {
            listener = orderBookStreamService.subscribe(ticker, board);
        }
        listener.getCollector();

        //TODO this is just for example, need to rework  !!!!!
    }


    private boolean hasRelevantListener(EventStreamListener listener) {
        return listener == null ||
                listener.getScheduledFuture().isDone() ||
                listener.getScheduledFuture().isCancelled();
    }
}
