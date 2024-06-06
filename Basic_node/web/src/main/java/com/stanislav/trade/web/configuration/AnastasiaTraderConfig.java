/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stanislav.trade.domain.service.grpc.GRpcConnection;
import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.domain.smart.impl.SmartAutoTradeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;


@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.stanislav.trade")
public class AnastasiaTraderConfig {

    private final String appId;
    private final String secretKey;
    private final String grpcServer;


    public AnastasiaTraderConfig(@Value("${grpc.service.appId}") String appId,
                                 @Value("${grpc.service.secretKey}") String secretKey,
                                 @Value("${grpc.service.resource}") String grpcServer) {
        this.appId = appId;
        this.secretKey = secretKey;
        this.grpcServer = grpcServer;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public GRpcConnection gRpcConnection() {
        return new GRpcConnection(grpcServer);
    }

    @Bean
    public SmartAutoTradeService smartAutoTradeService(@Autowired GRpcConnection connection) {
        return new SmartAutoTradeImpl(appId, secretKey, connection);
    }
}