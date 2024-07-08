package com.stanislav.trade.datasource;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatasourceConfig {

    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
    private RedisConnectionFactory redisConnectionFactory;

    private final Properties jpaProperties;
    private final Properties inMemoryProperties;


    public DatasourceConfig(@Value("${database.jpa.properties}") String jpa,
                            @Value("${database.in-memory.properties}") String inMemory) {
        jpaProperties = loadDatabaseProperties(jpa);
        inMemoryProperties = loadDatabaseProperties(inMemory);
    }

    // \/ <---------- JPA configuration ----------> \/ //

    @Bean
    public DataSource dataSource() {
        var dataSource = new JndiDataSourceLookup().getDataSource(jpaProperties.getProperty("datasource.name"));
        log.info("Datasource=" + dataSource);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.stanislav.trade.entities");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(jpaProperties);

        entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());

        return this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    // /\ <---------- JPA configuration ----------> /\ //
    // <---------------------------------------------> //
    // \/ <--------- Redis configuration ---------> \/ //

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        String host = inMemoryProperties.getProperty("redis.config.host");
        int port = Integer.parseInt(inMemoryProperties.getProperty("redis.config.port"));

        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration(host, port);

        var connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        redisConnectionFactory = connectionFactory;
        return connectionFactory;
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(JedisConnectionFactory connectionFactory) {

        final RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    private void destroyRedisConnectionFactory() {
        if (redisConnectionFactory != null) {
            try {
                var jcf =(JedisConnectionFactory) redisConnectionFactory;
                jcf.destroy();
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }
        }
    }

    // /\ <--------- Redis configuration ---------> /\ //
    // <---------------------------------------------> //

    @PreDestroy
    public void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.destroy();
        }
        destroyRedisConnectionFactory();
    }


    private Properties loadDatabaseProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     DatasourceConfig.class.getClassLoader().getResourceAsStream(fileName)) {

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}