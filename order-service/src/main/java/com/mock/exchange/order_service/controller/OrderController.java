package com.mock.exchange.order_service.controller;
import com.mock.exchange.order_service.client.UserClient;
import com.mock.exchange.order_service.entity.Order;
import com.mock.exchange.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order,
                                        Authentication authentication,
                                        @RequestHeader("Authorization") String token) {

        // set username from token info (Spring Security handles it)
        order.setUsername(authentication.getName());
        order.setStatus("PENDING");

        // Verify user from user-service
        String result = userClient.getUserInfo(order.getUsername(), token);
        System.out.println("User verified: " + result);

        Order saved = orderRepository.save(order);
        return ResponseEntity.ok(saved);
    }
}

