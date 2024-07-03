/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stanislav.trade.datasource.OrderDao;
import com.stanislav.trade.domain.market.finam.dto.FinamPortfolioDto;
import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamBuySell;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderResponse;
import com.stanislav.trade.entities.Broker;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.orders.Stop;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.entities.user.Portfolio;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.JsonDataParser;
import com.stanislav.trade.utils.RestConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.List;

import static com.stanislav.trade.domain.trading.finam.FinamTradingService.Args.*;

@Slf4j
@Service("finamTradingService")
public class FinamTradingService implements TradingService {

    private final JsonDataParser dataParser;
    private final RestConsumer restConsumer;
    private OrderDao orderDao;  //TODO !!!


    @Autowired
    public FinamTradingService(@Qualifier("jsonParser") ApiDataParser dataParser, RestConsumer restConsumer,
                               @Value("${api.finam}") String resource) {
        this.dataParser = (JsonDataParser) dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setAuthorization(RestConsumer.Authorization.API_KEY);
        this.restConsumer.setResource(resource);
    }


    @Override
    public Broker getBroker() {
        return Broker.Finam;
    }

    @Override
    public Portfolio getPortfolio(String clientId, String token) {
        GetQueryBuilder query = new GetQueryBuilder(Resource.PORTFOLIO.value);
        query.add(CLIENT.value, clientId)
                .add(POSITIONS.value, true)
                .add(CURRENCIES.value, true)
                .add(MONEY.value, false);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            var dto = dataParser.parseObject(response, FinamPortfolioDto.class, "data");
            return dto.toPortfolio();
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Order> getOrders(String clientId, String token, boolean matched, boolean canceled, boolean active) {
        GetQueryBuilder query = new GetQueryBuilder(Resource.ORDERS.value);
        query.add(CLIENT.value, clientId)
                .add(MATCHED.value, matched)
                .add(CANCELED.value, canceled)
                .add(ACTIVE.value, active);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, token);
            String[] layers = {"data", "orders"};
            List<FinamOrderResponse> dtoList = dataParser.parseObjectsList(response, FinamOrderResponse.class, layers);
            return dtoList.stream().map(FinamOrderResponse::toOrderClass).toList();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void makeOrder(Order order, TradeCriteria tradeCriteria) {
        FinamOrderTradeCriteria finamOrderCriteria = (FinamOrderTradeCriteria) tradeCriteria;
        FinamBuySell buySell = FinamBuySell.convert(order.getDirection());
        Account account = order.getAccount();
        FinamOrderRequest finamOrder = FinamOrderRequest.builder()
                .clientId(account.getClientId())
                .securityBoard(order.getBoard().toString())
                .securityCode(order.getTicker())
                .buySell(buySell)
                .quantity(order.getQuantity())
                .useCredit(finamOrderCriteria.useCredit())
                .price(order.getPrice().doubleValue())
                .property(finamOrderCriteria.property())
                .condition(finamOrderCriteria.condition())
                .validBefore(finamOrderCriteria.validBefore())
                .build();
        orderCriteria(finamOrder);

        try {
            String json = dataParser.getObjectMapper().writer().writeValueAsString(finamOrder);
            String response = restConsumer.doPostJson(Resource.ORDERS.value, json, account.getToken());
            int id = (int) dataParser.getJsonMap(response, "data").get("transactionId");
            order.setOrderId(id);
            orderDao.save(order);
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

    private void orderCriteria(FinamOrderRequest order) {

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