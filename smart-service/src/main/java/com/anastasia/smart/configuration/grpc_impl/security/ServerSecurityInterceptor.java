/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.smart.configuration.grpc_impl.security;

import io.grpc.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;

@GrpcGlobalServerInterceptor
public class ServerSecurityInterceptor implements ServerInterceptor {

    private static final String AUTHORIZATION_KEY = "X-Api-Key";

    private final String appId;
    private final JwtParser jwtParser;

    @Autowired
    public ServerSecurityInterceptor(@Value("${grpc.service.appId}") String appId,
                                     @Value("${grpc.service.secretKey}") String key) {
        this.appId = appId;
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String value = headers.get(Metadata.Key.of(AUTHORIZATION_KEY, Metadata.ASCII_STRING_MARSHALLER));
        Status status;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("token is not found");
        } else {
            try {
                String id = jwtParser.parseSignedClaims(value).getPayload().getId();
                if (signed(id)) {
                    Context context = Context.current();
                    return Contexts.interceptCall(context, call, headers, next);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (ExpiredJwtException | IllegalArgumentException
                     | SecurityException | MalformedJwtException e) {
                status = Status.UNAUTHENTICATED.withDescription("token is not valid");
            }
        }
        call.close(status, headers);
        return new ServerCall.Listener<>() {};
    }

    private boolean signed(String id) {
        return id.equals(appId);
    }
}
