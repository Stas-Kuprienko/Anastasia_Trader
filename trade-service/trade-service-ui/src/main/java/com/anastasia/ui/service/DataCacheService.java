package com.anastasia.ui.service;

import java.util.List;

public interface DataCacheService {

    <V> void put(String key, String field, V value);

    <V> void putAsJson(String key, String field, V value);

    <V> V get(String key, String field, Class<V> valueClass);

    <V> List<V> getListForKey(String key, Class<V> valueClass);

    <V> V getAndParseFromJson(String key, String field, Class<V> valueClass);

    <V> List<V> getAndParseListForKeyFromJson(String key, Class<V> valueClass);
}
