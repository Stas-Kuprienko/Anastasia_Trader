package com.anastasia.smart.controllers;

import com.anastasia.smart.configuration.grpc_impl.security.ServerSecurityInterceptor;
import com.anastasia.trade.Entities;
import com.anastasia.trade.HighsAndLowsGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService(interceptors = ServerSecurityInterceptor.class)
public class HighAndLowController extends HighsAndLowsGrpc.HighsAndLowsImplBase {

    @Override
    public void findLocalHighs(Entities.Instrument request, StreamObserver<Entities.PricePointList> responseObserver) {
        super.findLocalHighs(request, responseObserver);
    }

    @Override
    public void findLocalHighCandles(Entities.Instrument request, StreamObserver<Entities.PriceCandleList> responseObserver) {
        super.findLocalHighCandles(request, responseObserver);
    }

    @Override
    public void findLocalLows(Entities.Instrument request, StreamObserver<Entities.PricePointList> responseObserver) {
        super.findLocalLows(request, responseObserver);
    }

    @Override
    public void findLocalLowCandles(Entities.Instrument request, StreamObserver<Entities.PriceCandleList> responseObserver) {
        super.findLocalLowCandles(request, responseObserver);
    }
}
