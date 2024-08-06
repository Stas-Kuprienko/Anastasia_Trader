package com.stanislav.ui.service.impl;

import com.stanislav.ui.service.DataCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisHashCommands;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("dataCacheService")
public class MyRedisCacheService implements DataCacheService {

    private final RedisConnection connection;
    private final RedisHashCommands hashCommands;
    private final StringRedisSerializer stringSerializer;
    private final GenericJackson2JsonRedisSerializer genericSerializer;

    @Autowired
    public MyRedisCacheService(RedisConnectionFactory redisConnectionFactory,
                               StringRedisSerializer stringRedisSerializer,
                               GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        this.connection = redisConnectionFactory.getConnection();
        this.genericSerializer = genericJackson2JsonRedisSerializer;
        hashCommands = connection.hashCommands();
        this.stringSerializer = stringRedisSerializer;
    }

    @PreDestroy
    public void destroy() {
        connection.close();
    }


    @Override
    public <V> void put(@NonNull String key, @NonNull String field, @NonNull V value) {
        byte[] keyToBytes = stringSerializer.serialize(key);
        byte[] fieldToBytes = stringSerializer.serialize(field);
        byte[] valueToBytes = genericSerializer.serialize(value);
        if (fieldToBytes != null && keyToBytes != null && valueToBytes != null) {
            hashCommands.hSet(keyToBytes, fieldToBytes, valueToBytes);
        } else {
            throw new IllegalArgumentException("argument is null");
        }
    }

    @Override
    public <V> V get(@NonNull String key, @NonNull String field, @NonNull Class<V> valueClass) {
        byte[] keyToBytes = stringSerializer.serialize(key);
        byte[] fieldToBytes = stringSerializer.serialize(field);
        if (fieldToBytes != null && keyToBytes != null) {
            byte[] valueInBytes = hashCommands.hGet(keyToBytes, fieldToBytes);
            return genericSerializer.deserialize(valueInBytes, valueClass);
        } else {
            throw new IllegalArgumentException("argument is null");
        }
    }

    @Override
    public <V> List<V> getListForKey(@NonNull String key, @NonNull Class<V> valueClass) {
        byte[] keyToBytes = stringSerializer.serialize(key);
        if (keyToBytes != null) {
            Map<byte[], byte[]> map = hashCommands.hGetAll(keyToBytes);
            if (map != null) {
                ArrayList<V> result = new ArrayList<>();
                for (var v : map.values()) {
                    V object = genericSerializer.deserialize(v, valueClass);
                    result.add(object);
                }
                return result;
            } else {
                return Collections.emptyList();
            }
        } else {
            throw new IllegalArgumentException("argument is null");
        }
    }
}
