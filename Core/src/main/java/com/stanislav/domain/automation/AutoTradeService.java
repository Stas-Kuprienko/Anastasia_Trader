package com.stanislav.domain.automation;

import com.stanislav.event_stream.EventStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoTradeService {

    private final EventStreamService orderBookStreamService;

    public AutoTradeService(@Autowired EventStreamService eventStreamService) {
        this.orderBookStreamService = eventStreamService;
    }


}
