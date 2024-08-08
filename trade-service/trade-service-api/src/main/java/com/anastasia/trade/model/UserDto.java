package com.anastasia.trade.model;

public record UserDto(long id, String login, String password, String role, String name) {}
