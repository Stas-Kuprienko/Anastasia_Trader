package com.anastasia.ui.service.impl;

import com.anastasia.ui.configuration.AnastasiaUIConfig;
import com.anastasia.ui.configuration.auth.TokenAuthService;
import com.anastasia.ui.exception.BadRequestException;
import com.anastasia.ui.exception.NotFoundException;
import com.anastasia.ui.model.forms.NewUserForm;
import com.anastasia.ui.model.user.User;
import com.anastasia.ui.service.UserService;
import com.anastasia.ui.utils.GetRequestParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "users/";

    private final HttpHeaders authorizeHeaders;
    private final RestTemplate restTemplate;


    @Autowired
    public UserServiceImpl(TokenAuthService tokenAuthService,
                           RestTemplate restTemplate) {
        this.authorizeHeaders = tokenAuthService.authorize();
        this.restTemplate = restTemplate;
    }


    @Override
    public User registry(String login, String password, String name) {
        NewUserForm newUserForm = new NewUserForm(login, password, name);
        HttpEntity<NewUserForm> httpEntity = new HttpEntity<>(newUserForm, authorizeHeaders);
        ResponseEntity<User> response = restTemplate
                .exchange(resource + "user", HttpMethod.POST, httpEntity, User.class);
        if (response.hasBody() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new BadRequestException("user with login '%s' is failed".formatted(login));
        }
    }

    @Override
    public User logIn(String login, String password) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource + "login");
        getRequest.add("login", login)
                .add("password", password);
        ResponseEntity<User> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
        if (response.hasBody() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new BadRequestException("login=" + login + " password=" + password);
        }
    }

    @Cacheable(value = "user:id", keyGenerator = "keyGeneratorById")
    @Override
    public User findById(Long id) {
        ResponseEntity<User> response = restTemplate
                .exchange(resource + id, HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
        if (response.hasBody() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new NotFoundException("user id = " + id);
        }
    }

    @Cacheable(value = "user:login", keyGenerator = "keyGeneratorByParams")
    @Override
    public User findByLogin(String login) {
        GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource + "user");
        getRequest.add("login", login);
        ResponseEntity<User> response = restTemplate
                .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
        if (response.hasBody() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new NotFoundException("user login = " + login);
        }
    }
}