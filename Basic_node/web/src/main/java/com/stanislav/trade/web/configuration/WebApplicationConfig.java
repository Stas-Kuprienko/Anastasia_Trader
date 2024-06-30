package com.stanislav.trade.web.configuration;

import com.stanislav.trade.entities.user.User;
import com.stanislav.trade.web.authentication.rest.MyJwtFilter;
import com.stanislav.trade.web.service.ErrorCase;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static com.stanislav.trade.web.configuration.WebApplicationConfig.Requests.*;

@Slf4j
@Configuration
@ComponentScan("com.stanislav.trade.web")
@EnableWebMvc
@EnableWebSecurity
public class WebApplicationConfig extends AbstractSecurityWebApplicationInitializer implements WebMvcConfigurer {

    public static final String resource = "http://localhost:8081/anastasia/";
    public static final String entryPage = "login";

    @Autowired
    private MyJwtFilter myJwtFilter;


    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/style/**").addResourceLocations("/style/");
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
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return web -> web.ignoring().requestMatchers("/style/*");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        //TODO exception handling
        http.securityMatcher("/api/**").csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(myJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(AuthenticationProvider authenticationProvider, HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLogin ->
                        formLogin.loginPage("/login")
                                .usernameParameter("email")
                                .successForwardUrl("/login/auth")
                )
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(PERMIT_ALL.url).permitAll()
                        .requestMatchers(ANONYMOUS.url).anonymous()
                        .requestMatchers(AUTHENTICATED.url).authenticated()
                        .requestMatchers(USER.url).hasAuthority(User.Role.USER.toString())
                        .requestMatchers(ADMIN.url).hasAuthority(User.Role.ADMIN.toString())
                        .anyRequest().denyAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/error/" + ErrorCase.ACCESS_DENIED)
                        .authenticationEntryPoint((req, resp, ex) -> resp.sendRedirect(resource + entryPage))
                );

        return http.authenticationProvider(authenticationProvider).build();
    }


    enum Requests {

        PERMIT_ALL("/*", "/WEB-INF/views/**", "/telegram-bot/*"),
        ANONYMOUS("/login/*", "/sing-up/*"),
        AUTHENTICATED("/market/**", "/logout"),
        USER("/trade/**", "/user/**", "/smart/**"),
        ADMIN("/admin/**");

        final String[] url;

        Requests(String... url) {
            this.url = url;
        }
    }
}