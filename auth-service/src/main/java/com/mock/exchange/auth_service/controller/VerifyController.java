
package com.mock.exchange.auth_service.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class VerifyController {

    @GetMapping("/verify")
    public ResponseEntity<String> verify(Authentication authentication) {
        return ResponseEntity.ok("Verified user: " + authentication.getName());
    }
}
