package com.anastasia.ui.utils;

import com.anastasia.ui.configuration.auth.TokenAuthService;
import com.anastasia.ui.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collection;

@Component
public class MyRestClient {

    private final RestTemplate restTemplate;
    private final TokenAuthService tokenAuthService;

    @Autowired
    public MyRestClient(RestTemplate restTemplate, TokenAuthService tokenAuthService) {
        this.restTemplate = restTemplate;
        this.tokenAuthService = tokenAuthService;
    }


    public <R> ResponseEntity<R> get(User user, String url, Class<R> type) {

        HttpHeaders headers = tokenAuthService.authorize(user);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), type);
    }


    public <R> ResponseEntity<? extends Collection<R>> get(User user, String url,
                                                           ParameterizedTypeReference<? extends Collection<R>> type) {

        HttpHeaders headers = tokenAuthService.authorize(user);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), type);
    }


    public <R> ResponseEntity<R> post(User user, String url, Class<R> type) {

        HttpHeaders headers = tokenAuthService.authorize(user);
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), type);
    }


    public <P, R> ResponseEntity<R> post(User user, String url, P parameters, Class<R> type) {

        HttpHeaders headers = tokenAuthService.authorize(user);
        HttpEntity<P> httpEntity = new HttpEntity<>(parameters, headers);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, type);
    }


    public <R> ResponseEntity<R> delete(User user, String url, Class<R> type) {

        HttpHeaders headers = tokenAuthService.authorize(user);
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), type);
    }
}