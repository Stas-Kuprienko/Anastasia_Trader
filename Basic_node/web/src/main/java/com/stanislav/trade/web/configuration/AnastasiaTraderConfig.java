/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stanislav.trade.domain.service.grpc.GRpcConnection;
import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.domain.smart.impl.SmartAutoTradeImpl;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.stanislav.trade")
public class AnastasiaTraderConfig {

    private final String id;
    private final String secretKey;
    private final String grpcServer;


    public AnastasiaTraderConfig(@Value("${api.id}") String id,
                                 @Value("${api.secretKey}") String secretKey,
                                 @Value("${grpc.service.resource}") String grpcServer) {
        this.id = id;
        this.secretKey = secretKey;
        this.grpcServer = grpcServer;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
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
    public JwtBuilder jwtBuilder() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.builder()
                .id(id)
                .signWith(key);
    }

    @Bean
    public JwtParser jwtParser() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parser().verifyWith(key).build();
    }

    @Bean
    public GRpcConnection gRpcConnection() {
        return new GRpcConnection(grpcServer);
    }

    @Bean
    public SmartAutoTradeService smartAutoTradeService(GRpcConnection connection, JwtBuilder jwtBuilder) {
        return new SmartAutoTradeImpl(jwtBuilder.compact(), connection);
    }

    @Bean
    public MessageSource errorMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:error_messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }
}