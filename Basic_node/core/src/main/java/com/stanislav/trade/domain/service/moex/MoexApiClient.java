/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service.moex;

import com.stanislav.trade.utils.GetQueryBuilder;

import java.util.Map;

public class MoexApiClient {

    private final static char delimiter = '/';
    private final static String source = "https://iss.moex.com/iss";
    private final static String HISTORY = "history";
    private final static String ENGINES = "engines";
    private final static String MARKETS = "markets";
    private final static String SECURITIES = "securities";
    private final static String STATISTICS = "statistics";
    private final static String STOCK = "stock";
    private final static String CURRENCY = "currency";
    private final static String FUTURES = "futures";
    private final static String SHARES = "shares";
    private final static String FORTS = "forts";
    private final static String BONDS = "bonds";

    private final DocFormat format;
    private final StringBuilder url;


    MoexApiClient(DocFormat format) {
        this.format = format;
        this.url = new StringBuilder(source);
    }

    public static MoexApiClient moexApiJsonClient() {
        return new MoexApiClient(DocFormat.JSON);
    }

    public static MoexApiClient moexApiXmlClient() {
        return new MoexApiClient(DocFormat.XML);
    }


    public MoexApiClient history() {
        url.append(delimiter).append(HISTORY);
        return this;
    }

    public MoexApiClient engines() {
        url.append(delimiter).append(ENGINES);
        return this;
    }

    public MoexApiClient markets() {
        url.append(delimiter).append(MARKETS);
        return this;
    }

    public MoexApiClient securities() {
        url.append(delimiter).append(SECURITIES);
        return this;
    }

    public MoexApiClient statistics() {
        url.append(delimiter).append(STATISTICS);
        return this;
    }

    public MoexApiClient stock() {
        url.append(delimiter).append(STOCK);
        return this;
    }

    public MoexApiClient currency() {
        url.append(delimiter).append(CURRENCY);
        return this;
    }

    public MoexApiClient futures() {
        url.append(delimiter).append(FUTURES);
        return this;
    }

    public MoexApiClient shares() {
        url.append(delimiter).append(SHARES);
        return this;
    }

    public MoexApiClient forts() {
        url.append(delimiter).append(FORTS);
        return this;
    }

    public MoexApiClient bonds() {
        url.append(delimiter).append(BONDS);
        return this;
    }

    public String build() {
        String result = url.append(format).toString();
        url.setLength(0);
        url.append(source);
        return result;
    }

    public String build(Map<String, Object> parameters) {
        GetQueryBuilder query = new GetQueryBuilder(url.append(format).toString());
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            query.add(e.getKey(), e.getValue());
        }
        url.setLength(0);
        url.append(source);
        return query.build();
    }


    public enum DocFormat {
        JSON(".json"),
        XML(".xml");

        final String value;

        DocFormat(String value) {
            this.value = value;
        }
    }
}
