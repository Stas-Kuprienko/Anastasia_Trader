/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.utils;

import java.util.ArrayList;

public class GetRequestParametersBuilder {

    private final StringBuilder uri;
    private final ArrayList<Parameter> parameters;

    public GetRequestParametersBuilder(String url) {
        uri = new StringBuilder();
        if (url != null) {
            uri.append(url);
        }
        this.parameters = new ArrayList<>();
    }


    public GetRequestParametersBuilder() {
        uri = new StringBuilder();
        this.parameters = new ArrayList<>();
    }

    public GetRequestParametersBuilder appendToUrl(Object str) {
        if (str != null) {
            uri.append(str);
        }
        return this;
    }

    public GetRequestParametersBuilder add(String key, Object value) {
        if (value != null) {
            parameters.add(new Parameter(key, value));
        }
        return this;
    }

    public String build() {
        if (!parameters.isEmpty()) {
            if (!uri.isEmpty() && uri.charAt(uri.length() - 1) == '/') {
                uri.deleteCharAt(uri.length() - 1);
            }
            uri.append('?');
            for (Parameter n : parameters) {
                uri.append(n.key).append('=').append(n.value).append('&');
            }
            uri.deleteCharAt(uri.length() - 1);
        }
        return uri.toString();
    }

    private record Parameter(String key, Object value) {}

    @Override
    public String toString() {
        return "GetRequestParametersBuilder{" +
                "uri=" + uri +
                ", parameters=" + parameters +
                '}';
    }
}