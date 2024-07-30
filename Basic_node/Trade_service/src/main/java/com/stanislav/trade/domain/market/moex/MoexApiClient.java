/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.market.moex;

import com.stanislav.trade.utils.GetRequestParametersBuilder;

import java.util.Map;

public class MoexApiClient {

    private final static char delimiter = '/';
    private final static String source = "https://iss.moex.com/iss";
    private final static String HISTORY = "history";
    private final static String ENGINES = "engines";
    private final static String MARKETS = "markets";
    private final static String BOARDS = "boards";
    private final static String SECURITIES = "securities";
    private final static String STATISTICS = "statistics";

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

    public MoexApiClient engine(Engine engine) {
        validate(ENGINES, engine.toString());
        url.append(delimiter).append(engine);
        return this;
    }

    public MoexApiClient markets() {
        url.append(delimiter).append(MARKETS);
        return this;
    }

    public MoexApiClient market(Market market) {
        validate(MARKETS, market.toString());
        url.append(delimiter).append(market);
        return this;
    }

    public MoexApiClient boards() {
        url.append(delimiter).append(BOARDS);
        return this;
    }

    public MoexApiClient board(Board board) {
        validate(BOARDS, board.toString());
        url.append(delimiter).append(board);
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

    public MoexApiClient parameterFormat() {
        url.append(delimiter).append("%s");
        return this;
    }

    public String build() {
        String result = url.append(format.value).toString();
        url.setLength(0);
        url.append(source);
        return result;
    }

    public String build(Map<String, Object> parameters) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(url.append(format).toString());
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            query.add(e.getKey(), e.getValue());
        }
        url.setLength(0);
        url.append(source);
        return query.build();
    }

    
    private void validate(String param, String arg) {
        String[] str = url.toString().split("/");
        if (!(str[str.length - 1].equals(param))) {
            String message = url.toString() + '+' + arg;
            throw new IllegalArgumentException(message);
        }
    }


    public enum DocFormat {
        JSON(".json"),
        XML(".xml");

        final String value;

        DocFormat(String value) {
            this.value = value;
        }
    }

    public enum Engine {

        stock, currency, futures, commodity
    }

    public enum Market {

        shares, bonds, index, foreignshares,
        selt, otc,
        forts, options,
        futures
    }

    public enum Board {
        tqbr, tqtf
    }
}
