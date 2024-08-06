package com.stanislav.ui.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableCaching
public class DataCacheConfig {

    private final String redisHost;
    private final Integer redisPort;
    private final String redisPassword;
    private final Integer ttlHours;
    private RedisConnectionFactory redisConnectionFactory;


    public DataCacheConfig(@Value("${redis.config.host}") String redisHost,
                           @Value("${redis.config.port}") Integer redisPort,
                           @Value("${redis.config.password}") String redisPassword,
                           @Value("${redis.config.ttl}") Integer ttlHours) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.ttlHours = ttlHours;
    }

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
}
