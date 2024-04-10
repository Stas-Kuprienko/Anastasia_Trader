package org.stanislav.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.stanislav.domain.utils.ApiDataParser;

@Component(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestConsumer {

    private final RestTemplate restTemplate;
    private final ApiDataParser dataParser;

    private Authorization authorization;

    private String resource;

    private String token;

    public RestConsumer(@Autowired RestTemplate restTemplate,
                        @Autowired @Qualifier("jsonParser") ApiDataParser dataParser) {
        this.restTemplate = restTemplate;
        this.dataParser = dataParser;
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

    public String doPostJson(String uri, Object object, String token) {
        String json = dataParser.toJson(object);
        HttpHeaders headers = new HttpHeaders();
        if (authorization != null) {
            headers.add(authorization.value, token);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        uri = resource + uri;
        return restTemplate.postForObject(uri, request, String.class);
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