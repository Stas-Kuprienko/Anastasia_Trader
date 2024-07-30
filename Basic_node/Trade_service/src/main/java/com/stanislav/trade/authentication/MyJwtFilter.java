package com.stanislav.trade.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class MyJwtFilter extends OncePerRequestFilter {

    private final RestAuthService restAuthService;

    @Autowired
    public MyJwtFilter(RestAuthService restAuthService) {
        this.restAuthService = restAuthService;
    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain chain) throws IOException, ServletException {
        try {
            Authentication authentication = restAuthService.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);;
            try (final PrintWriter writer = response.getWriter()) {
                writer.println(e.getMessage());
                response.getWriter().flush();
            }
        }
        chain.doFilter(request, response);
    }
}