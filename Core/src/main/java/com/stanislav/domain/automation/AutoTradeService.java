package com.stanislav.domain.automation;

import com.stanislav.event_stream.OrderBookStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoTradeService {

    private final OrderBookStreamService orderBookStreamService;

    public AutoTradeService(@Autowired OrderBookStreamService orderBookStreamService) {
        this.orderBookStreamService = orderBookStreamService;
    }


}
