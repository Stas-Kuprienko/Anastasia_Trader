package com.stanislav.telegram_bot.domain.user_context;

import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Optional;

@Component
@Scope(AbstractBeanFactory.SCOPE_PROTOTYPE)
public class UserContext {

    private final HashMap<Attribute, Object> attributes;
    private Long chatId;

    public UserContext() {
        this.attributes = new HashMap<>(Attribute.values().length);
    }


    public <T> void addAttribute(Attribute key, T value) {
        attributes.put(key, value);
    }

    public Optional<Object> getAttribute(Attribute key) {
        return Optional.ofNullable(attributes.get(key));
    }

    public <T> Optional<T> getAttribute(Attribute key, Class<T> classType) {
        try {
            return Optional.ofNullable(classType.cast(attributes.get(key)));
        } catch (ClassCastException e) {
            //TODO logger
            return Optional.empty();
        }
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }


    public enum Attribute {
        ORDER,
        SECURITY,
        SMART_SUBSCRIBE
    }
}
