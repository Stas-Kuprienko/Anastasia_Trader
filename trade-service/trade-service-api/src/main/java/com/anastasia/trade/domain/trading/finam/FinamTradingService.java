/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.domain.trading.finam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.anastasia.trade.domain.market.finam.dto.FinamPortfolioDto;
import com.anastasia.trade.domain.trading.OrderCriteria;
import com.anastasia.trade.domain.trading.TradingService;
import com.anastasia.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.anastasia.trade.domain.trading.finam.order_dto.FinamOrderResponse;
import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.orders.Order;
import com.anastasia.trade.entities.orders.Stop;
import com.anastasia.trade.entities.user.Portfolio;
import com.anastasia.trade.utils.ApiDataParser;
import com.anastasia.trade.utils.GetRequestParametersBuilder;
import com.anastasia.trade.utils.JsonDataParser;
import com.anastasia.trade.utils.RestConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import static com.anastasia.trade.domain.trading.finam.FinamTradingService.Args.*;

@Slf4j
@Service("finamTradingService")
public class FinamTradingService implements TradingService {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String DATA = "data";
    private final JsonDataParser dataParser;
    private final RestConsumer restConsumer;
    private final OrderRequestAdaptor requestAdaptor;


    @Autowired
    public FinamTradingService(@Qualifier("jsonParser") ApiDataParser dataParser, RestConsumer restConsumer,
                               @Value("${api.finam}") String resource, OrderRequestAdaptor requestAdaptor) {
        this.dataParser = (JsonDataParser) dataParser;
        this.restConsumer = restConsumer;
        this.requestAdaptor = requestAdaptor;
        this.restConsumer.setAuthorization(RestConsumer.Authorization.API_KEY);
        this.restConsumer.setResource(resource);
    }


    @Override
    public Broker getBroker() {
        return Broker.Finam;
    }

    @Override
    public Portfolio getPortfolio(String clientId, String token, boolean withPositions) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(Resource.PORTFOLIO.value);
        query.add(CLIENT.value, clientId)
                .add(POSITIONS.value, withPositions)
                .add(CURRENCIES.value, true)
                .add(MONEY.value, false);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            var dto = dataParser.parseObject(response, FinamPortfolioDto.class, DATA);
            return dto.toPortfolio();
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Order> getOrders(String clientId, String token, boolean matched, boolean canceled, boolean active) {
        GetRequestParametersBuilder query = new GetRequestParametersBuilder(Resource.ORDERS.value);
        query.add(CLIENT.value, clientId)
                .add(MATCHED.value, matched)
                .add(CANCELED.value, canceled)
                .add(ACTIVE.value, active);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {DATA, "orders"};
            List<FinamOrderResponse> dtoList = dataParser.parseObjectsList(response, FinamOrderResponse.class, layers);
            return dtoList.stream().map(FinamOrderResponse::toOrderClass).toList();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Order makeOrder(OrderCriteria criteria, String token) {
        FinamOrderRequest finamOrder = requestAdaptor.parse(criteria);
        try {
            String json = dataParser.getObjectMapper().writer().writeValueAsString(finamOrder);
            String response = restConsumer.doPostJson(Resource.ORDERS.value, json, token);
            //TODO error handling
            response = dataParser.getObjectMapper().readTree(response).get(DATA).toString();
            int transactionId = (int) dataParser.getObjectMapper().readValue(response, HashMap.class).get(TRANSACTION_ID);
            return Order.builder()
                    .orderId(transactionId)
                    .clientId(criteria.getClientId())
                    .broker(Broker.Finam)
                    .ticker(criteria.getTicker())
                    .price(BigDecimal.valueOf(criteria.getPrice()))
                    .quantity(criteria.getQuantity())
                    .direction(criteria.getDirection())
                    .board(criteria.getBoard())
                    .status(FinamOrderResponse.OrderStatus.Active.toString())
                    .build();
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void cancelOrder(String clientId, String token, int orderId) {

    }

    @Override
    public List<Stop> getStops(String clientId, String token, boolean matched, boolean canceled, boolean active) {
        return null;
    }

    @Override
    public void makeStop(Stop stop) {

    }

    @Override
    public void cancelStop(String clientId, String token, int stopId) {

    }


    enum Args {
        CLIENT("ClientId"),
        MATCHED("IncludeMatched"),
        CANCELED("IncludeCanceled"),
        ACTIVE("IncludeActive"),
        POSITIONS("Content.IncludePositions"),
        CURRENCIES("Content.IncludeCurrencies"),
        MONEY("Content.IncludeMoney");

        final String value;

        Args(String value) {
            this.value = value;
        }
    }

    enum Resource {
        ORDERS("orders"),
        STOPS("stops"),
        PORTFOLIO("portfolio");

        final String value;

        Resource(String value) {
            this.value = value;
        }
    }
}