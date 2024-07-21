package com.stanislav.telegram_bot.rest.consumers;

import com.stanislav.telegram_bot.authentication.RestAuthService;
import com.stanislav.telegram_bot.entities.user.Account;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class UserDataRestConsumer {

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;
    private final String resource;


    @Autowired
    public UserDataRestConsumer(@Value("${api.resource}") String resource,
                                RestTemplate restTemplate, RestAuthService restAuthService) {
        this.restTemplate = restTemplate;
        this.authorization = restAuthService.authorize();
        this.resource = resource + "api/user/accounts";
    }


    public List<Account> getAccounts(long id) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("id", id);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parameters, authorization);
        var accounts = restTemplate.exchange(
                resource, HttpMethod.GET, httpEntity, Account[].class)
                .getBody();
        if (accounts == null || accounts.length == 0) {
            return Collections.emptyList();
        } else {
            return List.of(accounts);
        }
    }
}