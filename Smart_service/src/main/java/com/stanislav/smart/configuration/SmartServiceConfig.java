package com.stanislav.smart.configuration;

import com.stanislav.smart.domain.analysis.technical.AnalysisAideSupplier;
import com.stanislav.smart.domain.market.MarketDataProvider;
import com.stanislav.smart.domain.market.event_stream.EventStreamKit;
import com.stanislav.smart.domain.market.event_stream.finam.FinamGrpcEventStreamKit;
import com.stanislav.smart.domain.market.finam.FinamGRpcMarketDataProvider;
import com.stanislav.smart.domain.controller.DroneLauncher;
import com.stanislav.smart.service.grpc_impl.GRpcClient;
import com.stanislav.smart.service.grpc_impl.GRpcFrame;
import com.stanislav.smart.domain.controller.grpc_impl.GRpcDroneLauncher;
import com.stanislav.smart.domain.controller.grpc_impl.SmartAutoTradeGRpcImpl;
import com.stanislav.smart.service.grpc_impl.security.ServerSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan("com.stanislav.smart")
@PropertySource("classpath:application.properties")
public class SmartServiceConfig {

    private final String appId;
    private final String secretKey;
    private final int port;
    private final String apiResource;
    private final String apiToken;
    private final ScheduledExecutorService scheduledExecutorService;


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
        this.scheduledExecutorService  = Executors.newScheduledThreadPool(Integer.parseInt(threadPoolSize));
    }


    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return scheduledExecutorService;
    }

    @Bean
    public GRpcClient grpcClient() {
        return new GRpcClient(apiResource, apiToken);
    }

    @Bean
    public EventStreamKit eventStreamKit(@Autowired GRpcClient gRpcClient) {
        return new FinamGrpcEventStreamKit(scheduledExecutorService, gRpcClient);
    }

    @Bean
    public MarketDataProvider marketDataProvider(@Autowired GRpcClient gRpcClient) {
        return new FinamGRpcMarketDataProvider(gRpcClient);
    }

    @Bean
    public AnalysisAideSupplier analysisAideSupplier(@Autowired MarketDataProvider marketDataProvider) {
        return new AnalysisAideSupplier(marketDataProvider);
    }

    @Bean
    public DroneLauncher<?, ?> droneLauncher(@Autowired EventStreamKit eventStreamKit,
                                             @Autowired AnalysisAideSupplier analysisAideSupplier,
                                             @Autowired ScheduledExecutorService scheduledExecutorService) {
        return new GRpcDroneLauncher(eventStreamKit.getOrderBookStreamService(), analysisAideSupplier, scheduledExecutorService);
    }

    @Bean
    public GRpcFrame grpcFrame(@Autowired DroneLauncher<?, ?> droneLauncher) {
        ServerSecurityInterceptor interceptor = new ServerSecurityInterceptor(appId, secretKey);
        SmartAutoTradeGRpcImpl smartAutoTrade = new SmartAutoTradeGRpcImpl(droneLauncher);
        return new GRpcFrame(port, List.of(interceptor), List.of(smartAutoTrade));
    }

    @Bean
    public ApplicationContext context() {
        return new AnnotationConfigApplicationContext();
    }


    @PreDestroy
    public void destroy() {
        scheduledExecutorService.shutdown();
    }
}