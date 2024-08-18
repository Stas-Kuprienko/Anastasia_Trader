package com.anastasia.smart.controllers;

import com.anastasia.smart.configuration.grpc_impl.security.ServerSecurityInterceptor;
import com.anastasia.trade.Entities;
import com.anastasia.trade.SupportResistanceGrpc;
import com.anastasia.trade.SupportResistanceOuterClass;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService(interceptors = ServerSecurityInterceptor.class)
public class SupportResistanceController extends SupportResistanceGrpc.SupportResistanceImplBase {

    @Override
    public void supportLevels(SupportResistanceOuterClass.LevelParameters request, StreamObserver<Entities.PriceLevelList> responseObserver) {
        super.supportLevels(request, responseObserver);
    }

    @Override
    public void resistanceLevels(SupportResistanceOuterClass.LevelParameters request, StreamObserver<Entities.PriceLevelList> responseObserver) {
        super.resistanceLevels(request, responseObserver);
    }

    @Override
    public void supportZones(SupportResistanceOuterClass.ZoneParameters request, StreamObserver<Entities.PriceZoneList> responseObserver) {
        super.supportZones(request, responseObserver);
    }

    @Override
    public void resistanceZones(SupportResistanceOuterClass.ZoneParameters request, StreamObserver<Entities.PriceZoneList> responseObserver) {
        super.resistanceZones(request, responseObserver);
    }
}
