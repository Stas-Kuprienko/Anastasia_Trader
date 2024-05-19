/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanislav.trade.domain.smart.SmartAutoTradeService;
import com.stanislav.trade.domain.smart.impl.SmartAutoTradeImpl;
import com.stanislav.trade.domain.smart.service.GRpcConnection;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.stanislav")
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.stanislav.datasource.jpa_repositories")
@EnableTransactionManagement
public class AnastasiaTraderConfig implements WebMvcConfigurer {

    private final String appId;
    private final String secretKey;
    private final String grpcServer;

    private final Properties databaseProperties;


    public AnastasiaTraderConfig(@Value("${grpc.service.appId}") String appId,
                                 @Value("${grpc.service.secretKey}") String secretKey,
                                 @Value("${grpc.service.resource}") String grpcServer,
                                 @Value("${database.properties.file}") String fileName) {
        this.appId = appId;
        this.secretKey = secretKey;
        this.grpcServer = grpcServer;
        databaseProperties = loadDatabaseProperties(fileName);
    }


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                databaseProperties.getProperty("connection.url"),
                databaseProperties.getProperty("connection.username"),
                databaseProperties.getProperty("connection.password"));
        dataSource.setDriverClassName(databaseProperties.getProperty("connection.driver_class"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.stanislav.entities");
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(databaseProperties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
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
    public GRpcConnection gRpcConnection() {
        return new GRpcConnection(grpcServer);
    }

    @Bean
    public SmartAutoTradeService smartAutoTradeService(@Autowired GRpcConnection connection) {
        return new SmartAutoTradeImpl(appId, secretKey, connection);
    }


    private Properties loadDatabaseProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     AnastasiaTraderConfig.class.getClassLoader().getResourceAsStream(fileName)) {

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}