package com.stanislav.web.utils;

import java.util.List;
import java.util.Map;

public interface ApiDataParser {

    <T> T parseObject(String source, Class<T> clas);

    <T> T parseObject(String source, Class<T> clas, String... layers);

    <T> List<T> parseObjectsList(String source, Class<T> clas);

    <T> List<T> parseObjectsList(String source, Class<T> clas, String... layers);

    Map<String, Object> getJsonMap(String source, String... layers);

    String toJson(Object o);
}
