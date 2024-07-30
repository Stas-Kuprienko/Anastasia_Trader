package com.stanislav.trade.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestAuthService {

    private final JwtParser jwtParser;
    private final String id;

    @Autowired
    public RestAuthService(@Value("${api.id}") String id, JwtParser jwtParser) {
        this.jwtParser = jwtParser;
        this.id = id;
    }


    public Authentication authenticate(HttpServletRequest request) {
        String token = request.getHeader(AuthType.API.header);
        if (token == null || token.isEmpty()) {
            throw new BadCredentialsException("Token is null or empty.");
        }
        Claims claims;
        try {
            claims = jwtParser.parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.warn(e.getMessage());
            throw new BadCredentialsException(e.getMessage());
        }
        if (claims.getId().equals(id)) {
            String serviceName = claims.getIssuer();
            ApiKeyAuthentication authentication = new ApiKeyAuthentication(token, serviceName);
            authentication.setAuthenticated(true);
            return authentication;
        } else throw new BadCredentialsException("Token ID is wrong.");
    }


    public enum AuthType {

        API("X-Api-Key");

        public final String header;

        AuthType(String header) {
            this.header = header;
        }
    }
}