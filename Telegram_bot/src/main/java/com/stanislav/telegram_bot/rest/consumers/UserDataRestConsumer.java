package com.stanislav.telegram_bot.rest.consumers;

import com.stanislav.telegram_bot.authentication.RestAuthService;
import com.stanislav.telegram_bot.entities.user.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class UserDataRestConsumer {

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;

    private final String resource;


    @Autowired
    public UserDataRestConsumer(RestTemplate restTemplate, RestAuthService restAuthService,
                                @Value("${api.resource}") String resource) {
        this.restTemplate = restTemplate;
        this.authorization = restAuthService.authorize();
        this.resource = resource + "api/user/accounts";
    }


    public Account[] getAccounts(long id) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("id", id);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameters, authorization);
        return restTemplate.exchange(
                resource, HttpMethod.GET, httpEntity, Account[].class)
                .getBody();
    }
}