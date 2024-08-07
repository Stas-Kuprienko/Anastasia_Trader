/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.utils;

import java.util.List;
import java.util.Map;

public interface ApiDataParser {

    <T> T parseObject(String source, Class<T> clas);

    <T> T parseObject(String source, Class<T> clas, String... layers);

    <T> List<T> parseObjectsList(String source, Class<T> clas);

    <T> List<T> parseObjectsList(String source, Class<T> clas, String... layers);
}
