package com.stanislav.trade.web.configuration;

import com.stanislav.trade.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static com.stanislav.trade.web.configuration.WebSecurityConfig.Requests.*;

@Configuration
@ComponentScan("com.stanislav.trade.web")
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig extends AbstractSecurityWebApplicationInitializer implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver getInternalResourceViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

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
    public WebSecurityCustomizer ignoringCustomizer() {
        return web -> web.ignoring().requestMatchers("/style/**");
    }

    @Bean
    public SecurityFilterChain filterChain(@Autowired AuthenticationProvider authenticationProvider,
                                           @Autowired HttpSecurity http) throws Exception {
        // TODO temporary
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .successForwardUrl("/")
                                .usernameParameter("login")
                                .permitAll())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL.url).permitAll()
                        .requestMatchers(ANONYMOUS.url).anonymous()
                        .requestMatchers(AUTHENTICATED.url).authenticated()
                        .requestMatchers(USER.url).hasRole(User.Role.USER.toString())
                        .requestMatchers(ADMIN.url).hasRole(User.Role.ADMIN.toString())
                        .anyRequest().denyAll());

        return http.authenticationProvider(authenticationProvider).build();
    }

    @Bean
    public FilterChainProxy filterChainProxy(@Autowired SecurityFilterChain filterChain) {
        return new FilterChainProxy(filterChain);
    }


    enum Requests {

        PERMIT_ALL("/*", "/WEB-INF/views/**", "/error/**"),
        ANONYMOUS("/sign-in/*", "/login/*"),
        AUTHENTICATED("/market/**", "/logout"),
        USER("/trade/**", "/user/**", "/smart/**"),
        ADMIN("/admin/**");

        final String[] url;

        Requests(String... url) {
            this.url = url;
        }
    }
}