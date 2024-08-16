package com.anastasia.trade.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface RestAuthService {

    Authentication authenticate(HttpServletRequest request);
}
