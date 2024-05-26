package com.stanislav.trade.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.stanislav.trade.web.configuration.WebSecurityConfig.Requests.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL.url).permitAll()
                        .requestMatchers(ANONYMOUS.url).anonymous()
                        .requestMatchers(AUTHENTICATED.url).authenticated()) //TODO
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    enum Requests {

        PERMIT_ALL("/start"),
        ANONYMOUS("/login", "/signin"),
        AUTHENTICATED("/app/**");

        final String[] url;

        Requests(String... url) {
            this.url = url;
        }
    }
}