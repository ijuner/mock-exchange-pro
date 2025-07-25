package com.mock.exchange.user_service.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(name = "auth-service")
public interface AuthClient {
 @GetMapping("/api/auth/verify")
    String verify(@RequestHeader("Authorization") String token);
}
