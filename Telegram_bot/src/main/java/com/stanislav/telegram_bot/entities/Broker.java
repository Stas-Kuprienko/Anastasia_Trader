package com.stanislav.telegram_bot.entities;

public enum Broker {

    Finam("Финам");

    
    private final String title;

    Broker(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
