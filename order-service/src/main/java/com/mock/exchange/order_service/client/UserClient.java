package com.mock.exchange.order_service.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
  @GetMapping("/api/user/{username}")
    String getUserInfo(@PathVariable("username") String username,
                       @RequestHeader("Authorization") String token);
}
