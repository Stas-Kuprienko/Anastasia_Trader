package com.stanislav.smart.service.grpc_impl.security;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;

import java.util.concurrent.Executor;

public class AuthCallCredential extends CallCredentials {

    private final String apiToken;
    private final Headers header;

    AuthCallCredential(Headers header, String apiToken) {
        this.header = header;
        this.apiToken = apiToken;
    }

    public static AuthCallCredential XApiKeyAuthorization(String apiToken) {
        return new AuthCallCredential(Headers.API_KEY, apiToken);
    }


    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        executor.execute(() -> apply(metadataApplier));
    }


    private void apply(MetadataApplier applier) {
        try {
            Metadata metadata = new Metadata();
            Metadata.Key<String> key = Metadata.Key.of(header.value, Metadata.ASCII_STRING_MARSHALLER);
            metadata.put(key, apiToken);
            applier.apply(metadata);
        } catch (Exception e) {
            applier.fail(Status.UNAUTHENTICATED.withCause(e));
        }
    }


    public enum Headers {

        AUTHORIZATION("Authorization"),
        API_KEY("x-api-key"),
        BEARER("Bearer ");

        public final String value;

        Headers(String value) {
            this.value = value;
        }
    }
}