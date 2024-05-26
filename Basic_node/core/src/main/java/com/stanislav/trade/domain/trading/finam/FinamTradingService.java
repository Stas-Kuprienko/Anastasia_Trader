/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.trading.finam;

import com.stanislav.trade.datasource.OrderDao;
import com.stanislav.trade.domain.trading.TradeCriteria;
import com.stanislav.trade.domain.trading.TradingService;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamBuySell;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderRequest;
import com.stanislav.trade.domain.trading.finam.order_dto.FinamOrderResponse;
import com.stanislav.trade.entities.orders.Order;
import com.stanislav.trade.entities.orders.Stop;
import com.stanislav.trade.entities.user.Account;
import com.stanislav.trade.utils.ApiDataParser;
import com.stanislav.trade.utils.GetQueryBuilder;
import com.stanislav.trade.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

import static com.stanislav.trade.domain.trading.finam.FinamTradingService.Args.*;

@Service("finamTradingService")
public class FinamTradingService implements TradingService {

    private final FinamOrderRequest.Property ORDER_PROPERTY = FinamOrderRequest.Property.PutInQueue; //TODO fix

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;
    private OrderDao orderDao;  //TODO !!!


    public FinamTradingService(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                               @Autowired RestConsumer restConsumer,
                               @Value("${broker.finam}") String resource) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.restConsumer.setAuthorization(RestConsumer.Authorization.API_KEY);
        this.restConsumer.setResource(resource);
    }


    @Override
    public List<Order> getOrders(Account account, boolean matched, boolean canceled, boolean active) {
        GetQueryBuilder query = new GetQueryBuilder(Resource.ORDERS.value);
        query.add(CLIENT.value, account.getClientId())
                .add(MATCHED.value, matched)
                .add(CANCELED.value, canceled)
                .add(ACTIVE.value, active);
        try {
            String response = restConsumer.doRequest(query.build(), HttpMethod.GET, account.getToken());
            String[] layers = {"data", "orders"};
            List<FinamOrderResponse> dtoList = dataParser.parseObjectsList(response, FinamOrderResponse.class, layers);
            return dtoList.stream().map(FinamOrderResponse::toOrderClass).toList();
        } catch (HttpClientErrorException e) {
            //TODO
            return Collections.emptyList();
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

        String response = restConsumer.doPostJson(Resource.ORDERS.value, finamOrder, account.getToken());
        int id = (int) dataParser.getJsonMap(response, "data").get("transactionId");
        order.setOrderId(id);
        orderDao.save(order);
    }

    @Override
    public void cancelOrder(Account account, int orderId) {

    }

    @Override
    public List<Stop> getStops(Account account, boolean matched, boolean canceled, boolean active) {
        return null;
    }

    @Override
    public void makeStop(Stop stop) {

    }

    @Override
    public void cancelStop(Account account, int stopId) {

    }

    private void orderCriteria(FinamOrderRequest order) {

    }

    enum Args {
        CLIENT("ClientId"),
        MATCHED("IncludeMatched"),
        CANCELED("IncludeCanceled"),
        ACTIVE("IncludeActive");

        final String value;

        Args(String value) {
            this.value = value;
        }
    }

    enum Resource {
        ORDERS("orders"),
        STOPS("stops");

        final String value;

        Resource(String value) {
            this.value = value;
        }
    }
}