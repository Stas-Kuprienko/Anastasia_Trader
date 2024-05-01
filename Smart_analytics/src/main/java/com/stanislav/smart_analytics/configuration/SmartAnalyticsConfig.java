package com.stanislav.smart_analytics.configuration;

import com.stanislav.smart_analytics.domain.event_stream.EventStreamKit;
import com.stanislav.smart_analytics.domain.event_stream.finam.FinamGrpcEventStreamKit;
import com.stanislav.smart_analytics.service.grpc_impl.GRpcClient;
import com.stanislav.smart_analytics.domain.market.MarketDataProvider;
import com.stanislav.smart_analytics.domain.market.finam.FinamGRpcMarketDataProvider;
import com.stanislav.smart_analytics.service.grpc_impl.GRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.stanislav.smart_analytics")
@PropertySource("classpath:application.properties")
public class SmartAnalyticsConfig {

    private final String serverToken;
    private final int port;
    private final String apiResource;
    private final String apiToken;
    private final int threadPoolSize;


    public SmartAnalyticsConfig(@Value("${grpc.service.token}") String serverToken,
                                @Value("${grpc.service.port}") String port,
                                @Value("${grpc.api.resource}") String apiResource,
                                @Value("${grpc.api.token}") String apiToken,
                                @Value("${service.thread_pool_size}") String threadPoolSize) {
        this.serverToken = serverToken;
        this.port = Integer.parseInt(port);
        this.apiResource = apiResource;
        this.apiToken = apiToken;
        this.threadPoolSize = Integer.parseInt(threadPoolSize);
    }


    @Bean
    public GRpcServer grpcServer() {
        return new GRpcServer(serverToken, port);
    }

    @Bean
    public GRpcClient grpcClient() {
        return new GRpcClient(apiResource, apiToken, threadPoolSize);
    }

    @Bean
    public EventStreamKit eventStreamKit(@Autowired GRpcClient gRpcClient) {
        return new FinamGrpcEventStreamKit(gRpcClient);
    }

    @Bean
    public MarketDataProvider marketDataProvider(@Autowired GRpcClient gRpcClient) {
        return new FinamGRpcMarketDataProvider(gRpcClient);
    }
}