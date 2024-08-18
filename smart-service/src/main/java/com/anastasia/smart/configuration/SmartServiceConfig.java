package com.anastasia.smart.configuration;

import com.anastasia.smart.configuration.grpc_impl.GRpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan("com.anastasia.smart")
@PropertySource("classpath:application.properties")
public class SmartServiceConfig {

    //TODO configure TLS security

    private final String apiResource;
    private final String apiToken;
    private final ExecutorService executorService;

    public SmartServiceConfig(@Value("${grpc.api.resource}") String apiResource,
                              @Value("${grpc.api.token}") String apiToken) {
        this.apiResource = apiResource;
        this.apiToken = apiToken;
        this.executorService = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory());
    }


    @Bean
    public ExecutorService executorService() {
        return executorService;
    }

    @Bean
    public GRpcClient grpcClient() {
        return new GRpcClient(apiResource, apiToken);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}