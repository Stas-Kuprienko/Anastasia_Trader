package com.stanislav.domain.smart.service;

import com.stanislav.entities.user.Account;
import com.stanislav.event_stream.EventStreamListener;
import com.stanislav.event_stream.EventStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AutoTradeService {

    private final EventStreamService orderBookStreamService;

    public AutoTradeService(@Autowired @Qualifier("orderBookStreamService") EventStreamService orderBookStreamService) {
        this.orderBookStreamService = orderBookStreamService;
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
