package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.BadRequestException;
import com.stanislav.ui.exception.NotFoundException;
import com.stanislav.ui.model.Broker;
import com.stanislav.ui.model.forms.NewAccountForm;
import com.stanislav.ui.model.user.Account;
import com.stanislav.ui.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "users/";

    private final RestTemplate restTemplate;
    private final HttpHeaders authorization;

    @Autowired
    public AccountServiceImpl(RestTemplate restTemplate, TokenAuthService authService) {
        this.restTemplate = restTemplate;
        this.authorization = authService.authorize();
    }


    @Override
    public Account createAccount(long userId, String clientId, String broker, String token) {
        NewAccountForm form = new NewAccountForm(broker, clientId, token);
        String url = resource + userId +
                "/accounts" +
                '/' +
                "account";
        HttpEntity<NewAccountForm> httpEntity = new HttpEntity<>(form, authorization);
        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Account.class);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new BadRequestException(
                    "Account for user=%d with parameters: '%s:%s' is failed".formatted(userId, broker, clientId));
        }
    }

    @Cacheable(value = "account:parameters", keyGenerator = "keyGeneratorByParams")
    @Override
    public Account findByClientIdAndBroker(long userId, String clientId, Broker broker) {
        StringBuilder url = new StringBuilder(resource);
        String accountParams = broker.toString() + ':' + clientId;
        url.append(userId)
                .append("/accounts")
                .append('/')
                .append(accountParams);
        ResponseEntity<Account> response = restTemplate
                .exchange(url.toString(), HttpMethod.GET, new HttpEntity<>(authorization), Account.class);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            throw new NotFoundException(
                    "Account='%s' for user=%d is not found".formatted(accountParams, userId));
        }
    }

    @Cacheable(value = "account:userId", keyGenerator = "keyGeneratorById")
    @Override
    public List<Account> findByUserId(long userId) {
        String url = resource + userId +
                "/accounts";
        ParameterizedTypeReference<List<Account>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Account>> response = restTemplate
                .exchange(url, HttpMethod.GET, new HttpEntity<>(authorization), responseType);
        if (response.hasBody()) {
            return response.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "account:userId", keyGenerator = "keyGeneratorById"),
            @CacheEvict(value = "account:parameters", keyGenerator = "keyGeneratorByParams")})
    @Override
    public void deleteAccount(long userId, String clientId, Broker broker) {
        StringBuilder url = new StringBuilder(resource);
        String accountParams = broker.toString() + ':' + clientId;
        url.append(userId)
                .append("/accounts/")
                .append(accountParams);
        ResponseEntity<Boolean> response = restTemplate
                .exchange(url.toString(), HttpMethod.DELETE, new HttpEntity<>(authorization), Boolean.class);
        if (!response.hasBody() || Boolean.FALSE.equals(response.getBody())) {
            throw new BadRequestException(
                    "Account deletion with parameters: '%s:%s' for user=%d is failed".formatted(broker, clientId, userId));
        }
    }
}
