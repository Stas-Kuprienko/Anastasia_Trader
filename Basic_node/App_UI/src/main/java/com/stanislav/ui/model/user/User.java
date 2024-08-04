package com.stanislav.ui.model.user;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;

@RedisHash("user")
public class User implements Serializable {

    private Long id;
    private String login;
    private String password;
    private Role role;
    private String name;

    public User(Long id, String login, String password, Role role, String name) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public User() {}


    public enum Role {
        USER, ADMIN
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                '}';
    }
}
