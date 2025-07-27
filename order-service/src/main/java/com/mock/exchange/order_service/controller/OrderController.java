package com.mock.exchange.order_service.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mock.exchange.order_service.client.UserClient;
import com.mock.exchange.order_service.dto.OrderEventDTO;
import com.mock.exchange.order_service.entity.Order;
import com.mock.exchange.order_service.repository.OrderRepository;
import com.mock.exchange.order_service.service.KafkaProducerService;
import java.time.LocalDateTime;

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
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper; // Assuming you have an ObjectMapper bean configured

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order,
                                        Authentication authentication,
                                        @RequestHeader("Authorization") String token) throws JsonProcessingException {

        // set username from token info (Spring Security handles it)
        order.setUsername(authentication.getName());
        order.setStatus("PENDING");

        // Verify user from user-service
        String result = userClient.getUserInfo(order.getUsername(), token);
        System.out.println("User verified: " + result);

        Order saved = orderRepository.save(order);

        // Publish order event to Kafka
        // Assuming KafkaProducerService is autowired in this controller
        //ensure kafka message consistent, create OrderEventDTO
        OrderEventDTO event = OrderEventDTO.builder()
                .id(saved.getId())
                .symbol(saved.getSymbol())
                .side(saved.getSide())
                .price(saved.getPrice())
                .quantity(saved.getQuantity())
                .timestamp(saved.getTimestamp())
                .build();

        String payload = objectMapper.writeValueAsString(event);
        kafkaProducerService.publishOrderEvent(payload);

        return ResponseEntity.ok(saved);
    }
}

