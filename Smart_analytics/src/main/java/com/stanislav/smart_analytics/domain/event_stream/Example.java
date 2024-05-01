package com.stanislav.smart_analytics.domain.event_stream;

import com.stanislav.smart_analytics.domain.entities.Board;
import com.stanislav.smart_analytics.domain.entities.TimeFrame;
import com.stanislav.smart_analytics.domain.event_stream.finam.FinamOrderBookStream;
import com.stanislav.smart_analytics.domain.event_stream.finam.FinamOrderBookCollector;
import com.stanislav.smart_analytics.domain.event_stream.grpc_impl.GRpcClient;
import com.stanislav.smart_analytics.domain.event_stream.service.EventStreamListener;
import com.stanislav.smart_analytics.domain.market.MarketDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

@Controller
@RequestMapping("/test")
public class Example {

    @Autowired
    private MarketDataProvider marketDataProvider;

//    @GetMapping("/start")
    public void start() {

        Properties appProp = new Properties();
        try (InputStream input = Example.class.getClassLoader().getResourceAsStream("application.properties")) {
            appProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String resource = appProp.getProperty("api.resource");
        String token = appProp.getProperty("api.token");
        String ticker = "SiM4";

        EventStreamListener listener;
        try (GRpcClient client = new GRpcClient(resource, token, 1)) {
            FinamOrderBookStream streamService = new FinamOrderBookStream(client);
            streamService.subscribe(ticker, "FUT");
            listener = streamService.getEventStream(ticker);
            Thread.sleep(2000);
            FinamOrderBookCollector collector = (FinamOrderBookCollector) listener.getCollector();
            System.out.println(listener.getScheduledFuture().isDone());
            Thread.sleep(10000);
            streamService.unsubscribe(listener);
            System.out.print("is cancelled - ");
            System.out.println(listener.getScheduledFuture().isCancelled());
            System.out.print("is done - ");
            System.out.println(listener.getScheduledFuture().isDone());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(listener.getScheduledFuture().isDone());
    }

    @GetMapping("/go")
    public void test() {
        System.out.println(marketDataProvider
                .getDayCandles("SBER", Board.TQBR, TimeFrame.Day.D1,
                        LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-03"), 10));
    }
}
