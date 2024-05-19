/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.utils;


import java.util.Objects;

import java.util.ArrayList;
import java.util.Arrays;

public class GetQueryBuilder {

    private final String url;
    private final ArrayList<Node> parameters;

    public GetQueryBuilder(String url) {
        this.url = url == null ? "" : url;
        this.parameters = new ArrayList<>();
    }

    public GetQueryBuilder() {
        this.url = "";
        this.parameters = new ArrayList<>();
    }

    public GetQueryBuilder add(String key, Object value) {
        if (value != null) {
            parameters.add(new Node(key, value));
        }
        return this;
    }

    public String build() {
        if (parameters.isEmpty()) {
            return url;
        } else {
            StringBuilder str = new StringBuilder(url).append('?');
            for (Node n : parameters) {
                str.append(n.key).append('=').append(n.value).append('&');
            } str.deleteCharAt(str.length() - 1);
            return str.toString();
        }
    }

    private record Node(String key, Object value) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetQueryBuilder that)) return false;
        return Objects.equals(url, that.url) &&
                Arrays.equals(parameters.toArray(), ((GetQueryBuilder) o).parameters.toArray());

    }

    @Override
    public int hashCode() {
        return Objects.hash(url, Arrays.hashCode(parameters.toArray(new Node[]{})));
    }

    @Override
    public String toString() {
        return build();
    }
}