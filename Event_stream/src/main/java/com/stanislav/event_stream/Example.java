package com.stanislav.event_stream;

import com.stanislav.event_stream.finam.FinamOrderBookStreamService;
import com.stanislav.event_stream.finam.FinamOrderBookCollector;

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

        EventStreamListener listener;
        try (FinamOrderBookStreamService streamService = new FinamOrderBookStreamService(resource, token, 1)) {
            streamService.subscribe(ticker, "FUT");
            listener = streamService.getEventStream(ticker);
            Thread.sleep(2000);
            FinamOrderBookCollector collector = (FinamOrderBookCollector) listener.getCollector();
            System.out.println(listener.getScheduledFuture().isDone());
            System.out.println(collector.getAsks());
            Thread.sleep(2000);
            System.out.println(listener.getScheduledFuture().isDone());
            System.out.println(collector.getAsks());
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(listener.getScheduledFuture().isDone());
    }
}
