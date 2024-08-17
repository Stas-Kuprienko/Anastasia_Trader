package com.anastasia.ui.configuration.auth;

import com.anastasia.ui.model.user.User;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthService {

    private final JwtBuilder jwtBuilder;
    private final String serviceName;

    @Autowired
    public TokenAuthService(JwtBuilder jwtBuilder,
                            @Value("${application.serviceName}") String serviceName) {
        this.jwtBuilder = jwtBuilder;
        this.serviceName = serviceName;
    }

    public HttpHeaders authorize() {
        HttpHeaders headers = new HttpHeaders();
        String token = jwtBuilder.issuer(serviceName).compact();
        headers.add(AuthType.API.header, token);
        return headers;
    }

    public HttpHeaders authorize(User user) {
        HttpHeaders headers = new HttpHeaders();
        String token = jwtBuilder
                .issuer(serviceName)
                .claim("userId", user.getId())
                .claim("userRole", user.getRole())
                .compact();
        headers.add(AuthType.API.header, token);
        return headers;
    }


    public enum AuthType {

        API("X-Api-Key");

        public final String header;

        AuthType(String header) {
            this.header = header;
        }
    }
}
