package com.stanislav.smart.domain.trade.grpc_impl;

import com.google.protobuf.DoubleValue;
import com.stanislav.smart.entities.Broker;
import com.stanislav.smart.entities.Direction;
import com.stanislav.smart.entities.criteria.NewOrderCriteria;
import com.stanislav.smart.domain.trade.TradingService;
import com.stanislav.smart.domain.trade.grpc_impl.finam.NewOrderResponse;
import com.stanislav.smart.configuration.grpc_impl.GRpcClient;
import com.stanislav.smart.configuration.grpc_impl.security.AuthCallCredential;
import grpc.tradeapi.v1.OrdersGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.tradeapi.v1.Common;
import proto.tradeapi.v1.Orders;

@Service
public class FinamTradingService implements TradingService {

    OrdersGrpc.OrdersStub stub;

    @Autowired
    public FinamTradingService(GRpcClient rpcClient) {
        this.stub = OrdersGrpc.newStub(rpcClient.getChannel());
    }


    @Override
    public int makeOrder(String clientId, String token, NewOrderCriteria criteria) {
        //TODO conditions properties
        AuthCallCredential callCredential = AuthCallCredential.XApiKeyAuthorization(token);
        Orders.NewOrderRequest request = Orders.NewOrderRequest.newBuilder()
                .setClientId(clientId)
                .setSecurityCode(criteria.ticker())
                .setSecurityBoard(criteria.board().name())
                .setPrice(DoubleValue.of(criteria.price()))
                .setQuantity(criteria.quantity())
                .setBuySell(parse(criteria.direction()))
                .setUseCredit(false)
                .build();
        var newOrderResponse = new NewOrderResponse();
        stub.withCallCredentials(callCredential).newOrder(request, newOrderResponse);
        return newOrderResponse.getTransactionId();
    }

    @Override
    public void checkOrder(String clientId, String token, int orderId) {

    }

    @Override
    public void cancelOrder(String clientId, String token, int orderId) {

    }

    @Override
    public Broker getBroker() {
        return Broker.Finam;
    }


    private Common.BuySell parse(Direction direction) {
        return switch (direction) {
            case Buy -> Common.BuySell.BUY_SELL_BUY;
            case Sell -> Common.BuySell.BUY_SELL_SELL;
            case null, default -> throw new IllegalArgumentException(String.valueOf(direction));
        };
    }
}
