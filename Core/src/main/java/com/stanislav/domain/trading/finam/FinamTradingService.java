package com.stanislav.domain.trading.finam;

import com.stanislav.database.AccountPersistence;
import com.stanislav.database.DatabaseRepository;
import com.stanislav.database.OrderPersistence;
import com.stanislav.domain.trading.TradingService;
import com.stanislav.domain.trading.finam.order_dto.*;
import com.stanislav.entities.Board;
import com.stanislav.entities.orders.Order;
import com.stanislav.entities.orders.Stop;
import com.stanislav.entities.user.Account;
import com.stanislav.web.utils.ApiDataParser;
import com.stanislav.web.utils.GetQueryBuilder;
import com.stanislav.web.utils.RestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.stanislav.domain.trading.finam.FinamTradingService.Args.*;

@Service("finamTradingService")
public class FinamTradingService implements TradingService {

    private final FinamOrderRequest.Property ORDER_PROPERTY = FinamOrderRequest.Property.PutInQueue; //TODO fix

    private final ApiDataParser dataParser;
    private final RestConsumer restConsumer;

    private final AccountPersistence accountPersistence;
    private final OrderPersistence orderPersistence;


    public FinamTradingService(@Autowired @Qualifier("jsonParser") ApiDataParser dataParser,
                               @Autowired RestConsumer restConsumer,
                               @Autowired AccountPersistence accountPersistence,
                               @Autowired OrderPersistence orderPersistence,
                               @Value("${broker.finam}") String resource) {
        this.dataParser = dataParser;
        this.restConsumer = restConsumer;
        this.accountPersistence = accountPersistence;
        this.orderPersistence = orderPersistence;
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
            return dtoList.stream().map(o -> o.toOrderClass(accountPersistence)).toList();
        } catch (HttpClientErrorException e) {
            return new ArrayList<>();
            //TODO
        }
    }

    @Override
    public void makeOrder(Account account, Order order) {
        makeOrder(account, order, false);
    }

    @Override
    public void makeOrder(Account account, Order order, boolean useCredit) {
        FinamBuySell buySell = FinamBuySell.convert(order.getDirection());
        FinamOrderCondition condition = new FinamOrderCondition(
                FinamOrderCondition.Type.Ask, order.getPrice(), null);
        FInamOrderValidBefore validBefore = new FInamOrderValidBefore(
                FInamOrderValidBefore.Type.TillEndSession, LocalDateTime.now().toString());
        FinamOrderRequest finamOrder = FinamOrderRequest.builder()
                .clientId(account.getClientId())
                .securityBoard(Board.TQBR.toString())
                .securityCode(order.getTicker())
                .buySell(buySell)
                .quantity(order.getQuantity())
                .useCredit(useCredit)
                .price(order.getPrice().doubleValue())
                .property(ORDER_PROPERTY)
                .condition(condition)
                .validBefore(validBefore)
                .build();
        orderCriteria(finamOrder);

        String response = restConsumer.doPostJson(Resource.ORDERS.value, finamOrder, account.getToken());
        int id = (int) dataParser.getJsonMap(response, "data").get("transactionId");
        order.setId(id);
        orderPersistence.save(order);
    }

    @Override
    public void cancelOrder(Account account, int id) {

    }

    @Override
    public List<Stop> getStops(Account account) {
        return null;
    }

    @Override
    public void makeStop(Stop stop) {

    }

    @Override
    public void cancelStop(Account account, int id) {

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