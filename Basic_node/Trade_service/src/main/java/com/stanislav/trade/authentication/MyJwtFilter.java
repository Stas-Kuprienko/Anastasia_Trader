package com.stanislav.trade.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyJwtFilter extends GenericFilterBean {

    private final RestAuthService restAuthService;

    public MyJwtFilter(RestAuthService restAuthService) {
        this.restAuthService = restAuthService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Authentication authentication = restAuthService.authenticate((HttpServletRequest) request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            //TODO logger
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.setContentType(MediaType.APPLICATION_JSON_VALUE);;
            try (final PrintWriter writer = httpResp.getWriter()) {
                writer.println(e.getMessage());
                httpResp.getWriter().flush();
            }
        }
        chain.doFilter(request, response);
    }
}
