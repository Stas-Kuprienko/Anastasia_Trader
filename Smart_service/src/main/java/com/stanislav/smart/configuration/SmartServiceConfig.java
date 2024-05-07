package com.stanislav.smart.configuration;

import com.stanislav.smart.domain.market.MarketDataProvider;
import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.domain.market.event_stream.finam.FinamGrpcEventStreamKit;
import com.stanislav.smart.domain.market.finam.FinamGRpcMarketDataProvider;
import com.stanislav.smart.service.SmartService;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.service.grpc_impl.MySmartService;
import com.stanislav.smart.service.ScheduleDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.stanislav.smart")
@PropertySource("classpath:application.properties")
public class SmartServiceConfig {

    private final String appId;
    private final String secretKey;
    private final int port;
    private final String apiResource;
    private final String apiToken;
    private final int threadPoolSize;


    public SmartServiceConfig(@Value("${grpc.service.appId}") String appId,
                              @Value("${grpc.service.secretKey}") String secretKey,
                              @Value("${grpc.service.port}") String port,
                              @Value("${grpc.api.resource}") String apiResource,
                              @Value("${grpc.api.token}") String apiToken,
                              @Value("${service.thread_pool_size}") String threadPoolSize) {
        this.appId = appId;
        this.secretKey = secretKey;
        this.port = Integer.parseInt(port);
        this.apiResource = apiResource;
        this.apiToken = apiToken;
        this.threadPoolSize = Integer.parseInt(threadPoolSize);
    }

    @Bean
    public ScheduleDispatcher scheduleDispatcher() {
        return new ScheduleDispatcher(threadPoolSize);
    }

    @Bean
    public GRpcClient grpcClient() {
        return new GRpcClient(apiResource, apiToken);
    }

    @Bean
    public EventStreamKit eventStreamKit(@Autowired ScheduleDispatcher scheduleDispatcher,
                                         @Autowired GRpcClient gRpcClient) {
        return new FinamGrpcEventStreamKit(scheduleDispatcher, gRpcClient);
    }

    @Bean
    public SmartService smartService(@Autowired EventStreamKit eventStreamKit,
                                     @Autowired ScheduleDispatcher scheduleDispatcher) {
        return new MySmartService(scheduleDispatcher.getScheduledExecutor(), eventStreamKit.getOrderBookStreamService());
    }

    @Bean
    public MarketDataProvider marketDataProvider(@Autowired GRpcClient gRpcClient) {
        return new FinamGRpcMarketDataProvider(gRpcClient);
    }

    @Bean
    public ApplicationContext context() {
        return new AnnotationConfigApplicationContext();
    }
}