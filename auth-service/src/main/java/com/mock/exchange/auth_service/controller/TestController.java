package com.mock.exchange.auth_service.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// This controller exposes a protected endpoint that requires valid JWT
@RestController
public class TestController {
 // This endpoint can only be accessed if a valid token is passed in the Authorization header
    @GetMapping("/api/test")
    public String test(Authentication authentication) {
        // Authentication object is automatically injected by Spring Security
        return "Hello, " + authentication.getName() + "! This is a protected endpoint.";
    }
}
