package com.anastasia.telegram.domain.session;

import com.anastasia.telegram.entities.user.ContextState;

import java.util.HashMap;
import java.util.Optional;

public class SessionContext {

    private final HashMap<Attribute, Object> attributes;
    private long chatId;
    private ContextState contextState;
    private long lastActivity;


    public SessionContext() {
        this.attributes = new HashMap<>(Attribute.values().length << 1);
    }


    public <T> void addAttribute(Attribute key, T value) {
        updateLastActivity();
        attributes.put(key, value);
    }

    public Optional<Object> getAttribute(Attribute key) {
        updateLastActivity();
        return Optional.ofNullable(attributes.get(key));
    }

    public <T> Optional<T> getAttribute(Attribute key, Class<T> classType) {
        updateLastActivity();
        try {
            return Optional.ofNullable(classType.cast(attributes.get(key)));
        } catch (ClassCastException e) {
            //TODO logger
            return Optional.empty();
        }
    }

    public void reset() {
        attributes.clear();
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public ContextState getContextState() {
        return contextState;
    }

    public void setContextState(ContextState contextState) {
        this.contextState = contextState;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    private void updateLastActivity() {
        lastActivity = System.currentTimeMillis();
    }

    public enum Attribute {
        ORDER,
        SECURITY,
        SMART_SUBSCRIBE
    }
}