package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.exception.NotFoundException;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.forms.NewAccountForm;
import com.anastasia.ui.model.user.Account;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.AccountService;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "users/";

    private final UserService userService;
    private final MyRestClient myRestClient;


    @Autowired
    public AccountServiceImpl(UserService userService, MyRestClient myRestClient) {
        this.userService = userService;
        this.myRestClient = myRestClient;
    }


    @Override
    public Account createAccount(long userId, String clientId, String broker, String token) {
        NewAccountForm form = new NewAccountForm(broker, clientId, token);
        String url = resource + userId +
                "/accounts" +
                '/' +
                "account";
        User user = userService.findById(userId);
        ResponseEntity<Account> response = myRestClient.post(user, url, form, Account.class);
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
        User user = userService.findById(userId);
        ResponseEntity<Account> response = myRestClient.get(user, url.toString(), Account.class);
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
        User user = userService.findById(userId);
        ParameterizedTypeReference<List<Account>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<? extends Collection<Account>> response = myRestClient.get(user, url, responseType);
        if (response.hasBody()) {
            return (List<Account>) response.getBody();
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
        User user = userService.findById(userId);
        ResponseEntity<Boolean> response = myRestClient.delete(user, url.toString(), Boolean.class);
        if (!response.hasBody() || Boolean.FALSE.equals(response.getBody())) {
            throw new BadRequestException(
                    "Account deletion with parameters: '%s:%s' for user=%d is failed".formatted(broker, clientId, userId));
        }
    }
}
