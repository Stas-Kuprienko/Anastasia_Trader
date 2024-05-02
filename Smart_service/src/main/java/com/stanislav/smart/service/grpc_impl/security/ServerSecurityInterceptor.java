/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.stanislav.smart.service.grpc_impl.security;

import io.grpc.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;

import javax.crypto.spec.SecretKeySpec;

public class ServerSecurityInterceptor implements ServerInterceptor {

    private static final String AUTHORIZATION_KEY = "X-Api-Key";

    private final JwtParser jwtParser;

    public ServerSecurityInterceptor(String secretKey) {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), Jwts.SIG.HS256.getId());
        this.jwtParser = Jwts.parser().verifyWith(keySpec).build();
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String value = headers.get(Metadata.Key.of(AUTHORIZATION_KEY, Metadata.ASCII_STRING_MARSHALLER));
        Status status;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("token is not found");
        } else {
            try {
                jwtParser.parse(value);
                Context context = Context.current();
                return Contexts.interceptCall(context, call, headers, next);
            } catch (ExpiredJwtException | IllegalArgumentException
                     | SecurityException | MalformedJwtException e) {
                status = Status.UNAUTHENTICATED.withDescription("token is not valid");
            }
        }
        call.close(status, headers);
        return new ServerCall.Listener<>() {};
    }
}
