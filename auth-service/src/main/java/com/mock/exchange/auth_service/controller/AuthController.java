
package com.mock.exchange.auth_service.controller;
import com.mock.exchange.auth_service.dto.*;
import com.mock.exchange.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // User registration endpoint
    @PostMapping("/register")
    public JwtResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // User login endpoint
    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
