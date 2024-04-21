package com.stanislav.event_stream;

import com.stanislav.event_stream.finam.FinamOrderBookStreamService;
import com.stanislav.event_stream.finam.OrderBookStreamListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Example {

    public static void main(String[] args) {

        Properties appProp = new Properties();
        try (InputStream input = Example.class.getClassLoader().getResourceAsStream("application.properties")) {
            appProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String resource = appProp.getProperty("api.resource");
        String token = appProp.getProperty("api.token");
        String ticker = "SiM4";

        try {
            FinamOrderBookStreamService streamService = new FinamOrderBookStreamService(resource, token, 1);
            streamService.subscribe(ticker, "FUT");
            OrderBookStreamListener listener = streamService.getEventStream(ticker);
            Thread.sleep(2000);
            System.out.println(listener.getScheduledFuture().isDone());
            System.out.println(listener.getCollector().getAsks());
            Thread.sleep(2000);
            System.out.println(listener.getScheduledFuture().isDone());
            System.out.println(listener.getCollector().getAsks());
            streamService.shutdown();
            Thread.sleep(2000);
            System.out.println(listener.getScheduledFuture().isDone());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
