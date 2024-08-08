/*
 * Stanislav Kuprienko *** Omsk, Russia
 */

package com.anastasia.trade.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestConsumer {

    private final RestTemplate restTemplate;

    private Authorization authorization;

    private String resource;

    private String token;


    @Autowired
    public RestConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.resource = "";
    }


    public String doRequest(String uri, HttpMethod method) throws HttpStatusCodeException {
        uri = resource + uri;
        ResponseEntity<String> response;
        if (authorization != null && this.token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(authorization.value, this.token);
            HttpEntity <String> httpEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(uri, method, httpEntity, String.class);
        } else {
            response = restTemplate.exchange(uri, method, HttpEntity.EMPTY, String.class);
        }
        return response.getBody();
    }

    public String doRequest(String uri, HttpMethod method, String token) throws HttpStatusCodeException {
        uri = resource + uri;
        ResponseEntity<String> response;
        if (authorization != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(authorization.value, token);
            HttpEntity <String> httpEntity = new HttpEntity<>(headers);
            response = restTemplate.exchange(uri, method, httpEntity, String.class);
        } else {
            response = restTemplate.exchange(uri, method, HttpEntity.EMPTY, String.class);
        }
        return response.getBody();
    }

    public String doPostJson(String uri, String json, String token) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        if (authorization != null && token != null) {
            headers.add(authorization.value, token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        uri = resource + uri;
        return restTemplate.postForObject(uri, request, String.class);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public Authorization getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Authorization authorization) {
        this.authorization = authorization;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public enum Authorization {

        API_KEY("x-api-key"),
        BEARER("Bearer");

        public final String value;

        Authorization(String value) {
            this.value = value;
        }
    }
}