package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.exception.NotFoundException;
import com.anastasia.ui.model.Broker;
import com.anastasia.ui.model.forms.NewAccountForm;
import com.anastasia.ui.model.user.Account;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.AccountService;
import com.anastasia.ui.service.DataCacheService;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.MyRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "users/";
    private static final String cacheKey = "account";

    private final UserService userService;
    private final DataCacheService dataCacheService;
    private final MyRestClient myRestClient;


    @Autowired
    public AccountServiceImpl(UserService userService, DataCacheService dataCacheService, MyRestClient myRestClient) {
        this.userService = userService;
        this.dataCacheService = dataCacheService;
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
            return putToCache(userId, response.getBody());
        } else {
            throw new BadRequestException(
                    "Account for user=%d with parameters: '%s:%s' is failed".formatted(userId, broker, clientId));
        }
    }

    @Override
    public Account findByClientIdAndBroker(long userId, String clientId, Broker broker) {
        String accountParams = broker.toString() + ':' + clientId;
        var account = dataCacheService.getAndParseFromJson(cacheKey + ':' + userId, accountParams, Account.class);
        if (account != null) {
            return account;
        }
        String url = resource +
                userId +
                "/accounts" +
                '/' +
                accountParams;
        User user = userService.findById(userId);

        ResponseEntity<Account> response = myRestClient.get(user, url, Account.class);
        if (response.hasBody()) {
            return putToCache(userId, response.getBody());
        } else {
            throw new NotFoundException(
                    "Account='%s' for user=%d is not found".formatted(accountParams, userId));
        }
    }

    @Override
    public List<Account> findByUserId(long userId) {
        String key = cacheKey + ':' + userId;
        var accounts = dataCacheService.getAndParseListForKeyFromJson(key, Account.class);
        if (accounts != null && !accounts.isEmpty()) {
            return accounts;
        } else {
            String url = resource +
                    userId +
                    "/accounts";
            User user = userService.findById(userId);
            ParameterizedTypeReference<List<Account>> responseType = new ParameterizedTypeReference<>() {};

            ResponseEntity<? extends Collection<Account>> response = myRestClient.get(user, url, responseType);

            if (response.hasBody() && response.getBody() != null) {
                var accountList = (List<Account>) response.getBody();
                for (Account a : accountList) {
                    putToCache(userId, a);
                }
                return accountList;
            } else {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public void deleteAccount(long userId, String clientId, Broker broker) {
        StringBuilder url = new StringBuilder(resource);
        String accountParams = broker.toString() + ':' + clientId;
        url.append(userId)
                .append("/accounts/")
                .append(accountParams);
        User user = userService.findById(userId);

        ResponseEntity<Boolean> response = myRestClient.delete(user, url.toString(), Boolean.class);

        dataCacheService.remove(cacheKey + ':' + userId, accountParams);

        if (!response.hasBody() || Boolean.FALSE.equals(response.getBody())) {
            throw new BadRequestException(
                    "Account deletion with parameters: '%s:%s' for user=%d is failed".formatted(broker, clientId, userId));
        }
    }


    private Account putToCache(long userId, Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account is null");
        }
        String key = cacheKey + ':' + userId;
        String field = account.getBroker().toString() + ':' + account.getClientId();
        dataCacheService.putAsJson(key, field, account);
        return account;
    }
}
