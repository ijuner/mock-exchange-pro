package com.mock.exchange.auth_service.rate_limit;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter that intercepts /api/auth/login requests and applies rate limiting by IP.
 */
@Component
@RequiredArgsConstructor
public class LoginRateLimitFilter implements Filter{
 private final RateLimiterService rateLimiterService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;

        if (http.getRequestURI().equals("/api/auth/login")) {
            String ip = http.getRemoteAddr();

            if (!rateLimiterService.isAllowed(ip)) {
                throw new RateLimitExceededException("Too many login attempts. Please wait.");
            }
        }

        chain.doFilter(request, response);
    }
}
