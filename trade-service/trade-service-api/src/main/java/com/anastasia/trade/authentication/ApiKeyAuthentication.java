package com.anastasia.trade.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    private final String apiKey;
    private final String serviceName;

    public ApiKeyAuthentication(String apiKey, String serviceName) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.apiKey = apiKey;
        this.serviceName = serviceName;
    }

    //TODO to add more of user data

    @Override
    public Object getCredentials() {
        return serviceName;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
}