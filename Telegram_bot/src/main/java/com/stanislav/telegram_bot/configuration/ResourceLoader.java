package com.stanislav.telegram_bot.configuration;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import javax.crypto.SecretKey;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceLoader {

    private final String appId;
    private final String secretKey;


    public ResourceLoader(@Value("${api.appId}") String appId,
                          @Value("${api.secretKey}") String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(CharEncoding.UTF_8);
        return messageSource;
    }

    @Bean
    public JwtBuilder jwtBuilder() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.builder()
                .id(appId)
                .signWith(key);
    }

    @Bean
    public JwtParser jwtParser() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parser().verifyWith(key).build();
    }
}
