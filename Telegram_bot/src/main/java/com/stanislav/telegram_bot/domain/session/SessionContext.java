package com.stanislav.telegram_bot.domain.session;

import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Optional;

@Component
@Scope(AbstractBeanFactory.SCOPE_PROTOTYPE)
public class SessionContext {

    private final HashMap<Attribute, Object> attributes;
    private long chatId;
    private long lastActivity;


    public SessionContext() {
        this.attributes = new HashMap<>(Attribute.values().length);
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