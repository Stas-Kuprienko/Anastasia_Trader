package com.stanislav.event_stream;

import com.stanislav.event_stream.finam.FinamEventStreamService;

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

        try {
            FinamEventStreamService streamService = new FinamEventStreamService(resource, token);
            streamService.subscribe("SiM4", "FUT");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
