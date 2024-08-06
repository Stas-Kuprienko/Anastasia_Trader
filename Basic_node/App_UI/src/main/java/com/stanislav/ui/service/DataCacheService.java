package com.stanislav.ui.service;

import java.util.List;

public interface DataCacheService {

    <V> void put(String key, String field, V value);

    <V> V get(String key, String field, Class<V> valueClass);

    <V> List<V> getListForKey(String key, Class<V> valueClass);
}
