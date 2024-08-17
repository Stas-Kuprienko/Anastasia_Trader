package com.anastasia.trade.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collection;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyRestClient {

    private final RestTemplate restTemplate;
    private String authorization;

    @Autowired
    public MyRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.authorization = Authorization.API_KEY.value;
    }


    public <R> ResponseEntity<R> get(String url, String credentials, Class<R> type) {
        HttpHeaders headers = getHttpHeaders(credentials);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), type);
    }

    public <R> ResponseEntity<? extends Collection<R>> get(String url, String credentials,
                                                           ParameterizedTypeReference<? extends Collection<R>> type) {
        HttpHeaders headers = getHttpHeaders(credentials);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), type);
    }


    public <R> ResponseEntity<R> post(String url, String credentials, Class<R> type) {
        HttpHeaders headers = getHttpHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), type);
    }


    public <P, R> ResponseEntity<R> post(String url, String credentials, P parameters, Class<R> type) {
        HttpHeaders headers = getHttpHeaders(credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<P> httpEntity = new HttpEntity<>(parameters, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, type);
    }


    public <R> ResponseEntity<R> delete(String url, String credentials, Class<R> type) {
        HttpHeaders headers = getHttpHeaders(credentials);
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), type);
    }

    public String authorization() {
        return authorization;
    }

    public void setXApiKeyAuth() {
        authorization = Authorization.API_KEY.value;
    }

    public void setBearerAuth() {
        authorization = Authorization.BEARER.value;
    }

    public enum Authorization {

        API_KEY("x-api-key"),
        BEARER("Bearer");

        public final String value;

        Authorization(String value) {
            this.value = value;
        }
    }


    private HttpHeaders getHttpHeaders(String credentials) {
        HttpHeaders headers;
        if (credentials == null) {
            headers = HttpHeaders.EMPTY;
        } else {
            headers = new HttpHeaders();
            headers.add(authorization, credentials);
        }
        return headers;
    }
}