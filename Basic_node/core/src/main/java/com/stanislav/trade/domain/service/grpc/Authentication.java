/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.trade.domain.service.grpc;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.concurrent.Executor;

public class Authentication extends CallCredentials {

    private final String authorization;
    private final String token;


    public Authentication(String authorization, String token) {
        this.authorization = authorization;
        this.token = token;
    }


    public static String generateToken(String appId, String key) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        return Jwts.builder()
                .id(appId)
                .signWith(secretKey)
                .compact();
    }

    public static Authentication xApiKeyAuthorization(String token) {
        return new Authentication(Authorization.API_KEY.value, token);
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
