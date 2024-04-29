package com.stanislav.smart_analytics.configuration;

import com.stanislav.smart_analytics.event_stream.EventStreamKit;
import com.stanislav.smart_analytics.event_stream.grpc_impl.FinamGrpcEventStreamKit;
import com.stanislav.smart_analytics.service.SmartAutoTradeAPI;
import com.stanislav.smart_analytics.service.grpc_impl.GRpcAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.stanislav.smart_analytics")
@PropertySource("classpath:application.properties")
public class SmartAnalyticsConfig {

    private final String token;
    private final int port;
    private final String apiResource;
    private final String apiToken;
    private final int threadPoolSize;


    public SmartAnalyticsConfig(@Value("grpc.service.token") String token,
                                @Value("grpc.service.port") String port,
                                @Value("grpc.api.resource") String apiResource,
                                @Value("grpc.api.token") String apiToken,
                                @Value("service.thread_pool_size") String threadPoolSize) {
        this.token = token;
        this.port = Integer.parseInt(port);
        this.apiResource = apiResource;
        this.apiToken = apiToken;
        this.threadPoolSize = Integer.parseInt(threadPoolSize);
    }


    @Bean
    public SmartAutoTradeAPI smartAutoTradeAPI() {
        return new GRpcAPI(token, port);
    }

    @Bean
    public EventStreamKit eventStreamKit() {
        return new FinamGrpcEventStreamKit(apiResource, apiToken, threadPoolSize);
    }
}