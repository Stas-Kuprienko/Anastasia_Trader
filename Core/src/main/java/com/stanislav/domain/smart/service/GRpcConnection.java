package com.stanislav.domain.smart.service;

import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;

public class GRpcConnection {

    private final ManagedChannel channel;

    public GRpcConnection(String resource) {
        this.channel = ManagedChannelBuilder.forTarget(resource).build();
    }


    public ManagedChannel getChannel() {
        return channel;
    }

    public Authentication xApiKeyAuthorization(String token) {
        return new Authentication(Authorization.API_KEY.value, token);
    }

    @PreDestroy
    public void close() {
        channel.shutdownNow();
    }


    public static class Authentication extends CallCredentials {

        private final String authorization;
        private final String token;

        public Authentication(String authorization, String token) {
            this.authorization = authorization;
            this.token = token;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo,
                                         Executor appExecutor,
                                         MetadataApplier applier) {
            Metadata metadata = new Metadata();
            Metadata.Key<String> key = Metadata.Key.of(authorization, Metadata.ASCII_STRING_MARSHALLER);
            metadata.put(key, token);
            appExecutor.execute(() -> applier.apply(metadata));
        }
    }

    public enum Authorization {

        AUTHORIZATION("Authorization"),
        BEARER("Bearer "),
        API_KEY("X-Api-Key");

        public final String value;
        Authorization(String value) {
            this.value = value;
        }
    }
}