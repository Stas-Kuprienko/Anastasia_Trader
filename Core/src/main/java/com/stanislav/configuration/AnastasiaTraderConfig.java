package com.stanislav.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanislav.database.DatabaseRepository;
import com.stanislav.database.hibernate.HibernateRepository;
import com.stanislav.event_stream.EventStreamKit;
import com.stanislav.event_stream.grpc_impl.FinamGrpcEventStreamKit;
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

    private final String apiGRPCResource;

    private final String apiGRPCToken;

    private final int grpcThreadPoolSize;


    public AnastasiaTraderConfig(@Value("${grpc.api.resource}") String apiGRPCResource,
                                 @Value("${grpc.api.token}") String apiGRPCToken,
                                 @Value("${grpc.thread_pool_size}") String grpcThreadPoolSize) {
        this.apiGRPCResource = apiGRPCResource;
        this.apiGRPCToken = apiGRPCToken;
        this.grpcThreadPoolSize = Integer.parseInt(grpcThreadPoolSize);
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
    public EventStreamKit eventStreamKit() {
        return new FinamGrpcEventStreamKit(apiGRPCResource, apiGRPCToken, grpcThreadPoolSize);
    }
}