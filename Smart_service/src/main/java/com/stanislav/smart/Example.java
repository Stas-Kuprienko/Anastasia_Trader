package com.stanislav.smart;

import com.stanislav.smart.domain.analysis.technical.SimpleMovingAverageAide;
import com.stanislav.smart.domain.entities.Board;
import com.stanislav.smart.domain.entities.TimeFrame;
import com.stanislav.smart.domain.entities.candles.Candles;
import com.stanislav.smart.domain.event_stream.EventStreamListener;
import com.stanislav.smart.domain.event_stream.finam.FinamOrderBookStream;
import com.stanislav.smart.domain.event_stream.finam.FinamOrderBookCollector;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.domain.market.MarketDataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

//@Controller
//@RequestMapping("/test")
public class Example {

//    @Autowired
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

//    @GetMapping("/go")
    public void test() {
        Candles candles = (marketDataProvider
                .getDayCandles("SBER", Board.TQBR, TimeFrame.Day.D1,
                        LocalDate.now(), 30));
        SimpleMovingAverageAide sma = new SimpleMovingAverageAide(candles, 5);
        System.out.println(sma);
    }

//    @GetMapping("/goo")
    public void intraTest() {
        Candles candles = marketDataProvider.getIntraDayCandles("SBER", Board.TQBR, TimeFrame.IntraDay.H1,
                LocalDateTime.now().minusHours(4), 30);
        SimpleMovingAverageAide sma = new SimpleMovingAverageAide(candles, 5);
        System.out.println(sma);
    }
}
