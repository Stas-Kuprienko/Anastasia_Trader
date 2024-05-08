package com.stanislav.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanislav.database.DatabaseRepository;
import com.stanislav.database.hibernate.HibernateRepository;
import com.stanislav.domain.smart.SmartAutoTradeService;
import com.stanislav.domain.smart.impl.SmartAutoTradeImpl;
import com.stanislav.domain.smart.service.GRpcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("com.stanislav")
@PropertySource("classpath:application.properties")
public class AnastasiaTraderConfig implements WebMvcConfigurer {

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

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DatabaseRepository databaseRepository() {
        return new HibernateRepository("hibernate.cfg.xml");
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