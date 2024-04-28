package com.stanislav.event_stream;

import com.stanislav.event_stream.finam.FinamOrderBookStream;
import com.stanislav.event_stream.finam.FinamOrderBookCollector;
import com.stanislav.event_stream.grpc_impl.GRpcClient;
import com.stanislav.event_stream.service.EventStreamListener;

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
        try (GRpcClient client = new GRpcClient(resource, token, 1)) {
            FinamOrderBookStream streamService = new FinamOrderBookStream(client);
            streamService.subscribe(ticker, "FUT");
            listener = streamService.getEventStream(ticker);
            Thread.sleep(2000);
            FinamOrderBookCollector collector = (FinamOrderBookCollector) listener.getCollector();
            System.out.println(listener.getScheduledFuture().isDone());
            Thread.sleep(5000);
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
}
