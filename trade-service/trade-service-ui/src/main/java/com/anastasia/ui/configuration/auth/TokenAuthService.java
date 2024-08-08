package com.anastasia.ui.configuration.auth;

import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthService {

    private final JwtBuilder jwtBuilder;
    private final String token;

    @Autowired
    public TokenAuthService(JwtBuilder jwtBuilder,
                            @Value("${application.serviceName}") String serviceName) {
        this.jwtBuilder = jwtBuilder;
        this.token = buildToken(serviceName);
    }

    public HttpHeaders authorize() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthType.API.header, token);
        return headers;
    }


    private String buildToken(String serviceName) {
        return jwtBuilder.issuer(serviceName).compact();
    }


    public enum AuthType {

        API("X-Api-Key");

        public final String header;

        AuthType(String header) {
            this.header = header;
        }
    }
}
