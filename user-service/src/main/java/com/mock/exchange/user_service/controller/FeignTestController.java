package com.mock.exchange.user_service.controller;
import com.mock.exchange.user_service.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for testing Feign client functionality.
 */
@RestController
@RequiredArgsConstructor
public class FeignTestController {

     private final AuthClient authClient;

    @GetMapping("/api/user/verify-token")
    public String testToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return authClient.verify(token);
    }
}
