package com.stanislav.telegram_bot.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RestAuthService {

    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;
    private final String id;
    private final String token;

    @Autowired
    public RestAuthService(JwtBuilder jwtBuilder, JwtParser jwtParser,
                           @Value("${api.id}") String id,
                           @Value("${application.serviceName}") String serviceName) {
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
        this.id = id;
        this.token = buildToken(serviceName);
    }


    public Authentication authenticate(HttpServletRequest request) {
        String token = request.getHeader(Headers.API.value);
        if (token == null || token.isEmpty()) {
            throw new BadCredentialsException("Token is null or empty.");
        }
        Claims claims;
        try {
            claims = jwtParser.parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            //TODO logs
            throw new BadCredentialsException(e.getMessage());
        }
        if (claims.getId().equals(id)) {
            String serviceName = claims.getIssuer();
            ApiKeyAuthentication authentication = new ApiKeyAuthentication(token, serviceName);
            authentication.setAuthenticated(true);
            return authentication;
        } else throw new BadCredentialsException("Token ID is wrong.");
    }

    public String getToken() {
        return token;
    }


    private String buildToken(String serviceName) {
        return jwtBuilder.issuer(serviceName).compact();
    }


    public enum Headers {

        API("X-Api-Key");

        public final String value;

        Headers(String value) {
            this.value = value;
        }
    }
}
