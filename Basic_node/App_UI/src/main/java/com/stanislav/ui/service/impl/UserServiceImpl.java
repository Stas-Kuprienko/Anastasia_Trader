package com.stanislav.ui.service.impl;

import com.stanislav.ui.configuration.AnastasiaUIConfig;
import com.stanislav.ui.configuration.auth.TokenAuthService;
import com.stanislav.ui.exception.BadRequestException;
import com.stanislav.ui.exception.NotFoundException;
import com.stanislav.ui.model.user.User;
import com.stanislav.ui.service.UserService;
import com.stanislav.ui.utils.GetRequestParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String resource = AnastasiaUIConfig.BACKEND_RESOURCE + "users/user";

    private final HttpHeaders authorizeHeaders;
    private final RestTemplate restTemplate;

    //TODO TEMPORARY SOLUTION. REMAKE BY REDIS
    private final HashMap<Long, User> cacheById;
    private final HashMap<String, User> cacheByLogin;


    @Autowired
    public UserServiceImpl(TokenAuthService tokenAuthService,
                           RestTemplate restTemplate) {
        this.authorizeHeaders = tokenAuthService.authorize();
        this.restTemplate = restTemplate;
        cacheById = new HashMap<>();
        cacheByLogin = new HashMap<>();
    }


    @Override
    public User registry(String login, String password, String name) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("login", login);
        parameters.add("password", password);
        if (name != null && !name.isEmpty()) {
            parameters.add("name", name);
        }
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameters, authorizeHeaders);
        ResponseEntity<User> response = restTemplate
                .exchange(resource, HttpMethod.POST, httpEntity, User.class);
        User user = response.getBody();
        if (user != null) {
            cacheById.put(user.getId(), user);
            cacheByLogin.put(user.getLogin(), user);
            return user;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public User logIn(String login, String password) {
        User user = cacheByLogin.get(login);
        if (user != null) {
            return user;
        } else {
            GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource + "/login");
            getRequest.add("login", login)
                    .add("password", password);
            ResponseEntity<User> response = restTemplate
                    .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
            user = response.getBody();
            if (user != null) {
                cacheByLogin.put(user.getLogin(), user);
                cacheById.put(user.getId(), user);
                return user;
            } else {
                throw new BadRequestException("login=" + login + " password=" + password);
            }
        }
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = Optional.ofNullable(cacheById.get(id));
        if (user.isPresent()) {
            return user.get();
        } else {
            ResponseEntity<User> response = restTemplate
                    .exchange(resource + '/' + id, HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
            user = Optional.ofNullable(response.getBody());
            if (user.isPresent()) {
                User u = user.get();
                cacheById.put(u.getId(), u);
                cacheByLogin.put(u.getLogin(), u);
                return u;
            } else {
                throw new NotFoundException("user id = " + id);
            }
        }
    }

    @Override
    public User findByLogin(String login) {
        Optional<User> user = Optional.ofNullable(cacheByLogin.get(login));
        if (user.isPresent()) {
            return user.get();
        } else {
            GetRequestParametersBuilder getRequest = new GetRequestParametersBuilder(resource);
            getRequest.add("login", login);
            ResponseEntity<User> response = restTemplate
                    .exchange(getRequest.build(), HttpMethod.GET, new HttpEntity<>(authorizeHeaders), User.class);
            user = Optional.ofNullable(response.getBody());
            if (user.isPresent()) {
                User u = user.get();
                cacheById.put(u.getId(), u);
                cacheByLogin.put(u.getLogin(), u);
                return u;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}