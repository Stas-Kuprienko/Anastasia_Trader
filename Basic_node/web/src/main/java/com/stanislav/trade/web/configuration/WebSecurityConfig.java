package com.stanislav.trade.web.configuration;

import com.stanislav.trade.web.authentication.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public AuthenticationProvider authenticationProvider(@Autowired PasswordEncoder passwordEncoder,
                                                         @Autowired UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO temporary
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL.url).permitAll()
                        .requestMatchers(ANONYMOUS.url).anonymous()
                        .requestMatchers(AUTHENTICATED.url).authenticated()
                ).formLogin(AbstractAuthenticationFilterConfigurer::permitAll);


        return http.build();
    }

    enum Requests {

        PERMIT_ALL("/"),
        ANONYMOUS("/sign-in"),
        AUTHENTICATED("/main/**"),
        ADMIN("/admin/**");

        final String[] url;

        Requests(String... url) {
            this.url = url;
        }
    }
}