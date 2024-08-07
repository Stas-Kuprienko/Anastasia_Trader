package com.stanislav.trade.datasource;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableCaching
public class DatasourceConfig {

    private final Properties jpaProperties;
    private final String redisHost;
    private final Integer redisPort;
    private final String redisPassword;
    private final Integer ttlHours;

    private RedisConnectionFactory redisConnectionFactory;


    public DatasourceConfig(@Value("${database.jpa.properties}") String jpa,
                            @Value("${redis.config.host}") String redisHost,
                            @Value("${redis.config.port}") Integer redisPort,
                            @Value("${redis.config.password}") String redisPassword,
                            @Value("${redis.config.ttl}") Integer ttlHours) {
        jpaProperties = loadDatabaseProperties(jpa);
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.ttlHours = ttlHours;
    }

    // \/ <---------- JPA configuration ----------> \/ //

    @Bean
    public DataSource dataSource() {
        return new JndiDataSourceLookup().getDataSource(jpaProperties.getProperty("datasource.name"));
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.stanislav.trade.entities");
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setPersistenceProvider(vendorAdapter.getPersistenceProvider());
        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    // /\ <---------- JPA configuration ----------> /\ //
    // <---------------------:::---------------------> //
    // \/ <--------- Redis configuration ---------> \/ //

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(redisHost, redisPort);

//        configuration.setPassword(redisPassword);

        var connectionFactory = new JedisConnectionFactory(configuration);
        redisConnectionFactory = connectionFactory;
        return connectionFactory;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer()
                .configure(o -> o.registerModule(new JavaTimeModule()));
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        var keySerializer = RedisSerializationContext
                .SerializationPair
                .fromSerializer(stringRedisSerializer());

        var valueSerializer = RedisSerializationContext
                .SerializationPair
                .fromSerializer(genericJackson2JsonRedisSerializer());

        return RedisCacheConfiguration
                .defaultCacheConfig().entryTtl(Duration.ofHours(ttlHours))
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(valueSerializer);
    }

    @Bean
    public CacheManager cacheManager() {
        return RedisCacheManager
                .builder(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration())
                .build();
    }

    @Bean("keyGeneratorById")
    public KeyGenerator keyGeneratorById() {
        return (target, method, params) -> params[0];
    }

    @Bean("keyGeneratorByParams")
    public KeyGenerator keyGeneratorByParams() {
        return (target, method, params) -> {
            StringBuilder str = new StringBuilder();
            Arrays.stream(params).forEach(p -> str.append(p.toString()).append(':'));
            return str.deleteCharAt(str.length() - 1)
                    .toString();
        };
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(JedisConnectionFactory connectionFactory) {

        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    // /\ <--------- Redis configuration ---------> /\ //
    // <---------------------:::---------------------> //

    @PreDestroy
    public void destroy() {
        if (redisConnectionFactory != null) {
            try {
                var jcf =(JedisConnectionFactory) redisConnectionFactory;
                jcf.destroy();
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }
        }
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